package repository;

import helpers.ConfigHelper;
import helpers.ContextHelper;
import models.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserRepository extends BaseCRUDRepository<User> implements IUserRepository {

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, ConfigHelper configHelper, ContextHelper contextHelper) {
        super(User.class, ebeanConfig, executionContext, configHelper, contextHelper);
    }

    @Override
    public CompletionStage<Optional<User>> findByUsername(String username) {
        return supplyAsync(() -> ebeanServer.find(User.class).where().ieq("username", username).findOneOrEmpty(), executionContext);
    }

}
