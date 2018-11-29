package actions;

import dtos.TokenData;
import models.User;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import services.TokenService;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static play.test.Helpers.*;

public class AuthMeActionTests extends WithApplication {
    @Test
    public void test_authenticatedCall_notauthorized() {
        Result result = route(app, controllers.routes.UserController.findAll("0", "10", "", "", ""));
        assertThat(result.status()).isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void test_authenticatedCall_authorized() {
        TokenService tokenService = app.injector().instanceOf(TokenService.class);
        User user = new User();
        user.setId(1L);
        String token = tokenService.generateToken(new TokenData(user));
        Http.RequestBuilder req = fakeRequest(controllers.routes.UserController.findAll("0", "10", "", "", "")).header("X-AUTH-TOKEN", token);
        Result result = route(app, req);
        assertThat(result.status()).isEqualTo(OK);
    }
}
