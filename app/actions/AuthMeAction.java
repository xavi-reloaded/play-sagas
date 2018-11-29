package actions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.inject.Inject;
import dtos.TokenData;
import enumerations.UserProfileEnum;
import helpers.ContextHelper;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import services.TokenService;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AuthMeAction extends AuthenticatedAction<IAdminAction> {
    @Inject
    private ContextHelper contextHelper;
    @Inject
    private TokenService tokenService;

    @Override
    public CompletionStage<Result> call(Http.Context context) {
        Optional<String> tokenOptional = getToken(context);
        if (!tokenOptional.isPresent()) return renderPromisedUnauthorized("Token not informed");
        String token = tokenOptional.get();
        TokenData tokenData;
        try {
            tokenData = tokenService.verifyTokenAndGetData(token);
        } catch (TokenExpiredException expired) {
            return supplyAsync(() -> status(498, expired.getMessage()));
        } catch (Exception e) {
            return supplyAsync(Results::unauthorized);
        }
        if (tokenData == null) return renderPromisedUnauthorized("Token is not valid");
        Optional<UserProfileEnum> profileOptional = this.getProfile();
        if (profileOptional.isPresent()) {
            UserProfileEnum requiredProfile = profileOptional.get();
            UserProfileEnum userProfile = tokenData.getUser().getProfile();
            if (requiredProfile.equals(UserProfileEnum.ADMIN) && !userProfile.equals(UserProfileEnum.ADMIN)) {
                return renderPromiseForbidden("Required admin profile to perform that action");
            }
        }
        this.contextHelper.setCurrentToken(token);
        return delegate.call(context);
    }

    protected Optional<UserProfileEnum> getProfile() {
        return Optional.empty();
    }

}