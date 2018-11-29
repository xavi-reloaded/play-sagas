package repository;

import models.User;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface IUserRepository extends IBaseCRUDRepository<User> {

    CompletionStage<Optional<User>> lookup(Long id);

    CompletionStage<Optional<User>> findByUsername(String username);

    CompletionStage<User> update(User user);

    CompletionStage<Optional<Long>> delete(Long id);

    CompletionStage<User> insert(User user);
}
