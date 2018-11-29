package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dtos.PagedListDTO;
import helpers.ContextHelper;
import models.BaseModel;
import org.junit.Test;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public abstract class BaseCRUDControllerTests <T extends BaseModel, S extends BaseCRUDController> extends WithApplication {

    public HttpExecutionContext httpExecutionContext;
    public ContextHelper contextHelper;

    abstract S getSut();
    abstract S getSutWithHttpContext(Http.Context context);
    protected abstract ObjectNode getUpdateJson();
    protected abstract ObjectNode getInsertJson();
    abstract Class<T> getModelClass();

    @Test
    public void test_lookup() throws ExecutionException, InterruptedException, IOException {
        CompletionStage<Result> lookup = this.getSut().lookup(1L);
        Result result = lookup.toCompletableFuture().get();
        assertThat(result.status()).isEqualTo(OK);
        ObjectMapper mapper = new ObjectMapper();
        String content = contentAsString(result);
        T t = mapper.readValue(content, this.getModelClass());
        assertThat(t.getId()).isNotNull();
    }

    @Test
    public void test_insert() throws ExecutionException, InterruptedException, IOException {
        Http.Context context = getTransactionRequest(getInsertJson());
        CompletionStage<Result> insertStage = this.getSutWithHttpContext(context).insert();
        validateTransactionResponse(insertStage);
    }

    private void validateTransactionResponse(CompletionStage<Result> insertStage) throws InterruptedException, ExecutionException, IOException {
        Result result = insertStage.toCompletableFuture().get();
        assertThat(result.status()).isEqualTo(OK);
        T t = new ObjectMapper().readValue(contentAsString(result), this.getModelClass());
        assertThat(t.getId()).isNotNull();
    }

    @Test
    public void test_update() throws ExecutionException, InterruptedException, IOException {
        Http.Context context = getTransactionRequest(getUpdateJson());
        CompletionStage<Result> insertStage = this.getSutWithHttpContext(context).update();
        validateTransactionResponse(insertStage);
    }

    @Test
    public void test_delete() throws ExecutionException, InterruptedException {
        CompletionStage<Result> deleteStage = this.getSut().delete(1L);
        Result delete = deleteStage.toCompletableFuture().get();
        assertThat(delete.status()).isEqualTo(OK);
        assertThat(Long.parseLong(contentAsString(delete))).isEqualTo(1L);
    }

    @Test
    public void test_findAll() throws ExecutionException, InterruptedException, IOException {
        CompletionStage<Result> findAllStage = this.getSut().findAll("0", "10", null, null, null);
        Result findAll = findAllStage.toCompletableFuture().get();
        assertThat(findAll.status()).isEqualTo(OK);
        PagedListDTO<T> pagedList = new ObjectMapper().readValue(contentAsString(findAll), PagedListDTO.class);
        assertThat(pagedList.getData().size()).isGreaterThan(0);
    }

    private Http.Context getTransactionRequest(ObjectNode objectNode) {
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.bodyJson(objectNode);
        return httpContext().withRequest(requestBuilder.build());
    }

}
