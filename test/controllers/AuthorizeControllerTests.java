package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.SessionDTO;
import exceptions.NotPossibleValidateTokenException;
import exceptions.WrongLoginException;
import helpers.validator.ValidatorHelper;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import services.IAuthorizeService;

import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class AuthorizeControllerTests extends WithApplication{

    private HttpExecutionContext httpExecutionContext;
    private ValidatorHelper validatorHelperMock;

    @Before
    public void setUp(){
        this.httpExecutionContext = app.injector().instanceOf(HttpExecutionContext.class);
        this.validatorHelperMock = mock(ValidatorHelper.class);
    }

    @Test
    public void test_login_ok() throws ExecutionException, InterruptedException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("username", "exist");
        objectNode1.put("password", "1234");
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.bodyJson(objectNode1);
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.login().toCompletableFuture().get();
        SessionDTO actual = mapper.readValue(contentAsString(loginResult), SessionDTO.class);
        assertThat(OK).isEqualTo(loginResult.status());
        assertThat("thisisthetoken").isEqualTo(actual.getToken());
    }

    @Test
    public void test_login_error() throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("username", "notexist");
        objectNode1.put("password", "1234");
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.bodyJson(objectNode1);
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.login().toCompletableFuture().get();
        assertThat(loginResult.status()).isEqualTo(401);
        assertThat(contentAsString(loginResult)).isEqualTo("{\"errorCode\":1640,\"message\":\"Email or password is wrong.\"}");
    }

    @Test
    public void test_login_error_no_username_password() throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("username", "");
        objectNode1.put("password", "");
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.bodyJson(objectNode1);
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.login().toCompletableFuture().get();
        assertThat(401).isEqualTo(loginResult.status());
        assertThat(contentAsString(loginResult)).isEqualTo("{\"errorCode\":1640,\"message\":\"Email or password is wrong.\"}");
    }

    @Test
    public void test_refreshtoken_ok() throws ExecutionException, InterruptedException {
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.header("X-AUTH-TOKEN", "thisisvalidtoken");
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.getNewSessionToken("validrefreshtoken").toCompletableFuture().get();
        assertThat(OK).isEqualTo(loginResult.status());
        assertThat("\"validnewtoken\"").isEqualTo(contentAsString(loginResult));
    }

    @Test
    public void test_refreshToken_fail() throws ExecutionException, InterruptedException {
        Http.RequestBuilder requestBuilder = fakeRequest();
        requestBuilder.header("X-AUTH-TOKEN", "thisisvalidtoken");
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.getNewSessionToken("invalidrefreshtoken").toCompletableFuture().get();
        assertThat(loginResult.status()).isEqualTo(401);
        assertThat(contentAsString(loginResult)).isEqualTo("No token found");
    }

    @Test
    public void test_refreshToken_noProvidedToken() throws ExecutionException, InterruptedException {
        Http.RequestBuilder requestBuilder = fakeRequest();
        Http.Context context = httpContext().withRequest(requestBuilder.build());
        AuthorizeController sut = new AuthorizeController(new AuthorizeServiceStub(), httpExecutionContext, context, this.validatorHelperMock);
        Result loginResult = sut.getNewSessionToken("invalidrefreshtoken").toCompletableFuture().get();
        assertThat(loginResult.status()).isEqualTo(401);
        assertThat(contentAsString(loginResult)).isEqualTo("No token found");
    }
}

class AuthorizeServiceStub implements IAuthorizeService{

    @Override
    public CompletionStage<SessionDTO> login(String username, String password) {
        if(username.equals("exist")){
            return supplyAsync(()-> new SessionDTO(new User(), "thisisthetoken", "thisistherefreshtoken"));
        }else{
            return supplyAsync(()->{
                throw new CompletionException(new WrongLoginException());
            });
        }
    }

    @Override
    public String generateNewTokenFromRefreshToken(String refreshToken) throws NotPossibleValidateTokenException {
        if(refreshToken.equals("validrefreshtoken")){
            return "validnewtoken";
        }
        throw new NotPossibleValidateTokenException();
    }
}