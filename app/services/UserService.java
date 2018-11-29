package services;

import exceptions.UsernameTakenException;
import models.User;
import play.libs.concurrent.HttpExecutionContext;
import repository.IUserRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by RP on 1/7/16.
 */
public class UserService extends WithPictureService<User, IUserRepository> implements IUserService {

    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public UserService(UserRepository repository, AssetService assetService, HttpExecutionContext httpExecutionContext) {
        super(repository, assetService);
        this.httpExecutionContext = httpExecutionContext;
    }

    @Override
    public CompletionStage<Optional<User>> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public CompletionStage<User> createUser(User user, String password) {
        return this.findByUsername(user.getUsername()).thenApplyAsync(
                optionalUser -> {
                    if (optionalUser.isPresent()) {
                        throw new CompletionException(new UsernameTakenException());
                    } else {
                        user.setPassword(password);
                        try {
                            return super.insert(user).toCompletableFuture().get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new CompletionException(e);
                        }
                    }
                }
                , this.httpExecutionContext.current());
    }
}