package controllers;

import actions.Authenticated;
import actions.IAdminAction;
import com.fasterxml.jackson.databind.JsonNode;
import helpers.ContextHelper;
import models.BaseModel;
import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Results;
import services.IBaseCRUDService;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Created by ecobos on 6/9/17.
 */
public abstract class BaseCRUDController<T extends BaseModel, S extends IBaseCRUDService<T>> extends BaseController {

    protected final S service;
    protected final HttpExecutionContext httpExecutionContext;
    protected final Class<T> modelClass;
    protected final ContextHelper contextHelper;

    public BaseCRUDController (Class<T> modelClass, S service, HttpExecutionContext httpExecutionContext, ContextHelper contextHelper){
        this.service = service;
        this.httpExecutionContext = httpExecutionContext;
        this.modelClass = modelClass;
        this.contextHelper = contextHelper;
    }

    @IAdminAction
    public CompletionStage<Result> lookup(Long id){
        return this.service.lookup(id)
                .thenApplyAsync(model -> this.returnOk(Json.toJson(model.get())), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }

    @Authenticated
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(){
        T jsonModel = getJsonModel();
        return this.service.update(jsonModel)
                .thenApplyAsync(createdModel -> this.returnOk(Json.toJson(createdModel)), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }

    @Authenticated
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> insert(){
        T jsonModel = getJsonModel();
        return this.service.insert(jsonModel)
                .thenApplyAsync(createdModel -> this.returnOk(Json.toJson(createdModel)), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }

    protected T getJsonModel() {
        JsonNode body = this.getHttpContext().request().body().asJson();
        return Json.fromJson(body, this.modelClass);
    }

    @Authenticated
    public CompletionStage<Result> delete(Long id){
        return this.service.delete(id)
                .thenApplyAsync(deletedId -> this.returnOk(Json.toJson(deletedId.get())), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }

    @Authenticated
    public CompletionStage<Result> findAll(String page, String pageSize, String fields, String search, String sort){
        Integer pageInt = Integer.parseInt(page);
        Integer pageSizeInt = Integer.parseInt(pageSize);
        return this.service.findAll(pageInt, pageSizeInt,fields, search, sort)
                .thenApplyAsync(list -> this.returnOk(Json.toJson(list)), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }

    @Authenticated
    public CompletionStage<Result> findAllByUser(String page, String pageSize, String fields, String search, String sort){
        User currentuser;
        try {
            currentuser = this.contextHelper.getCurrentUser(this.getHttpContext().request().getHeaders().get("X-AUTH-TOKEN").get());
        } catch (Exception e) {
            return supplyAsync(Results::unauthorized);
        }
        Integer pageInt = (page!=null) ? Integer.parseInt(page) : null;
        Integer pageSizeInt = (pageSize!=null) ? Integer.parseInt(pageSize) : null;
        return this.service.findAllByUser(currentuser, pageInt, pageSizeInt,fields, search, sort)
                .thenApplyAsync(list -> this.returnOk(Json.toJson(list)), httpExecutionContext.current())
                .exceptionally(error -> this.returnError(error.getCause()));
    }
}