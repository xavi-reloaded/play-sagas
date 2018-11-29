package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.BaseAppException;
import helpers.validator.exceptions.BaseValidationAppException;
import helpers.validator.exceptions.IBaseValidationAppException;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by ecobos on 6/13/17.
 */
public abstract class BaseController extends Controller {

    protected Http.Context httpContext;

    public Http.Context getHttpContext() {
        return (httpContext!=null) ? httpContext : Http.Context.current();
    }

    public Result returnOk(JsonNode json){
        if(json == null) {
            return ok();
        }else{
            return ok(json);
        }
    }

    Result returnError(Throwable e){
        if(e.getCause() instanceof BaseAppException){
            return this.returnError((Exception) e.getCause());
        }
        else if(e instanceof BaseAppException ){
            return this.returnError((Exception) e);
        }
        else{
            return badRequest(Json.toJson(e));
        }
    }

    Result returnError(BaseAppException e) {
        return status(e.getCode(), Json.toJson(e.getBody()));
    }

    Result returnError(BaseValidationAppException e) {
        return status(e.getCode(), Json.toJson(e.getBody()));
    }

    Result returnError(Exception e) {
        if (e instanceof IBaseValidationAppException) {
            return this.returnError((BaseValidationAppException) e);
        } else if (e instanceof BaseAppException) {
            return this.returnError((BaseAppException) e);
        } else {
            return badRequest(Json.toJson(e));
        }
    }
}
