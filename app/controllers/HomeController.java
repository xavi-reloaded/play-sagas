package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Singleton;

/**
 * Manage a database of computers
 */
@Singleton
public class HomeController extends Controller {

    public Result index() {
        return ok(views.html.index.render());
    }
}
            
