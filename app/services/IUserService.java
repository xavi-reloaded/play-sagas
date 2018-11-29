package services;

import models.User;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface IUserService extends IBaseCRUDService<User> {
    CompletionStage<Optional<User>> findByUsername(String username);

    CompletionStage<User> createUser(User user, String password);
}
