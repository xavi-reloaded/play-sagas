package controllers;

import org.junit.Test;
import play.mvc.Result;
import play.test.WithApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

public class HomeControllerTests extends WithApplication{

    @Test
    public void checkHomePage() {
        Result result = route(app, controllers.routes.HomeController.index());
        assertThat(result.status()).isEqualTo(OK);
    }
}
