package services;

import models.User;
import models.UserDevice;
import repository.UserDeviceRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by RP on 1/7/16.
 */
@Singleton
public class UserDeviceService extends BaseCRUDService<UserDevice, UserDeviceRepository> {

    @Inject
    public UserDeviceService(UserDeviceRepository repository) {
        super(repository);
    }

    public CompletionStage<List<UserDevice>> getUserDevices(User user) {
        return this.repository.getUserDevices(user);
    }
}
