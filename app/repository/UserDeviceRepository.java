package repository;

import helpers.ConfigHelper;
import helpers.ContextHelper;
import models.User;
import models.UserDevice;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserDeviceRepository extends BaseCRUDRepository<UserDevice>{

    @Inject
    public UserDeviceRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, ConfigHelper configHelper, ContextHelper contextHelper) {
        super(UserDevice.class, ebeanConfig, executionContext, configHelper, contextHelper);
    }

    public CompletionStage<List<UserDevice>> getUserDevices(User user){
        return supplyAsync(()->this.ebeanServer.find(this.modelClass).where().eq("user", user).findList(), executionContext);
    }
}
