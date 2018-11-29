package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.ContextHelper;
import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.IUserService;
import services.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of computers
 */
@Singleton
public class UserController extends BaseCRUDController<User, IUserService> {

    @Inject
    public UserController(UserService userService, HttpExecutionContext httpExecutionContext, ContextHelper contextHelper) {
        super(User.class, userService, httpExecutionContext, contextHelper);
    }

    public UserController(IUserService userService, HttpExecutionContext httpExecutionContext, ContextHelper contextHelper){
        super(User.class, userService, httpExecutionContext, contextHelper);
    }

    public UserController(IUserService userService, HttpExecutionContext httpExecutionContext, Http.Context context, ContextHelper contextHelper){
        super(User.class, userService, httpExecutionContext, contextHelper);
        this.httpContext = context;
    }

    @Override
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> insert() {
        JsonNode body = this.getHttpContext().request().body().asJson();
        User userJson = Json.fromJson(body, User.class);
        return this.service.createUser(userJson, Json.fromJson(body.get("password"), String.class))
                .thenApplyAsync(user -> this.returnOk(Json.toJson(user)), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }
}
            
