package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dtos.PagedListDTO;
import helpers.ContextHelper;
import models.User;
import org.junit.Before;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import services.IUserService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserControllerTests extends BaseCRUDControllerTests{
    public HttpExecutionContext httpExecutionContext;

    @Before
    public void setUp(){
        this.httpExecutionContext = app.injector().instanceOf(HttpExecutionContext.class);
        this.contextHelper = app.injector().instanceOf(ContextHelper.class);
    }

    @Override
    BaseCRUDController getSut() {
        return new UserController(new UserServiceStub(), this.httpExecutionContext, this.contextHelper);
    }

    @Override
    BaseCRUDController getSutWithHttpContext(Http.Context context) {
        return new UserController(new UserServiceStub(), this.httpExecutionContext, context, this.contextHelper);
    }

    @Override
    protected ObjectNode getUpdateJson() {
        User user = new User();
        user.setId(1L);
        return (ObjectNode) Json.toJson(user);
    }

    @Override
    Class getModelClass() {
        return User.class;
    }

    @Override
    protected ObjectNode getInsertJson() {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.node.ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.put("username", "newusername");
        objectNode1.put("password", "1234");
        return objectNode1;
    }


}

class UserServiceStub implements IUserService{

    @Override
    public CompletionStage<Optional<User>> findByUsername(String username) {
        return null;
    }

    @Override
    public CompletionStage<User> createUser(User user, String password) {
        return supplyAsync(()->{
            user.setPassword(password);
            user.setId(1L);
            return user;
        });
    }

    @Override
    public CompletionStage<Optional<User>> lookup(Long id) {
        return supplyAsync(()->{
            User user = new User();
            user.setId(id);
            return Optional.of(user);
        });
    }

    @Override
    public CompletionStage<User> update(User model) {
        return supplyAsync(()->{
            model.setId(1L);
            return model;
        });
    }

    @Override
    public CompletionStage<User> insert(User model) {
        return supplyAsync(()->{
            model.setId(1L);
            return model;
        });
    }

    @Override
    public CompletionStage<Optional<Long>> delete(Long id) {
        return supplyAsync(()->Optional.of(id));
    }

    @Override
    public CompletionStage<PagedListDTO<User>> findAll(Integer page, Integer pageSize, String fields, String search, String sort){
        return supplyAsync(()->{
            ArrayList<User> users = new ArrayList<>();
            users.add(new User());
            return new PagedListDTO<User>(users);
        });
    }

    @Override
    public CompletionStage<PagedListDTO<User>> findAllByUser(User user, Integer page, Integer pageSize, String fields, String search, String sort) {
        return this.findAll(page, pageSize, fields, search, sort);
    }
}