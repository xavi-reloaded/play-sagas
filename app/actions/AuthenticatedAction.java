package actions;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by xavi on 9/21/16.
 */
public abstract class AuthenticatedAction<T> extends Action<T> {

    public CompletionStage<Result> renderPromisedUnauthorized(String apiMessage) {
        return CompletableFuture.completedFuture(unauthorized(apiMessage));
    }

    public CompletionStage<Result> renderPromiseForbidden(String apiMessage) {
        return CompletableFuture.completedFuture(forbidden(apiMessage));
    }

    public Optional<String> getToken(Http.Context context){
        Http.Headers headers = context.request().getHeaders();
        return headers.get("X-AUTH-TOKEN");
    }
}
