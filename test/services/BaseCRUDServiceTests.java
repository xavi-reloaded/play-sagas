package services;

import dtos.PagedListDTO;
import models.BaseModel;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.awaitility.Awaitility.await;

public abstract class BaseCRUDServiceTests <T extends BaseModel, S extends IBaseCRUDService<T>> {

    public S sut;
    abstract T getModel();

    @Test
    public void test_lookup(){
        CompletionStage<Optional<T>> found = this.sut.lookup(1L);
        await().atMost(1, SECONDS).until(() ->
                assertThat(found.toCompletableFuture()).isCompletedWithValueMatching(model -> (model.isPresent()))
        );
    }

    @Test
    public void test_update(){
        CompletionStage<T> updated = this.sut.update(this.getModel());
        await().atMost(1, SECONDS).until(() ->
                assertThat(updated.toCompletableFuture()).isCompletedWithValueMatching(model -> (model.getId()!=null))
        );
    }

    @Test
    public void test_insert(){
        CompletionStage<T> inserted = this.sut.insert(this.getModel());
        await().atMost(1, SECONDS).until(() ->
                assertThat(inserted.toCompletableFuture()).isCompletedWithValueMatching(model -> (model.getId()!=null))
        );
    }

    @Test
    public void test_delete(){
        CompletionStage<Optional<Long>> deleted = this.sut.delete(1L);
        await().atMost(1, SECONDS).until(() ->
                assertThat(deleted.toCompletableFuture()).isCompletedWithValueMatching(model -> (model.isPresent()))
        );
    }

    @Test
    public void test_findAll() {
        CompletionStage<PagedListDTO<T>> list = this.sut.findAll(0, 10, "", "", "");
        await().atMost(1, SECONDS).until(() ->
                assertThat(list.toCompletableFuture()).isCompletedWithValueMatching(pagedList -> (pagedList.getData().size()>=1))
        );
    }
}
