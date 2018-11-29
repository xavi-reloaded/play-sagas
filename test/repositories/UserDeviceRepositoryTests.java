package repositories;

import enumerations.UserProfileEnum;
import models.User;
import models.UserDevice;
import org.junit.Before;
import org.junit.Test;
import repository.UserDeviceRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDeviceRepositoryTests extends BaseCrudRepositoriesTest<UserDevice, UserDeviceRepository> {

    @Before
    public void setUp() {
        this.sut = app.injector().instanceOf(UserDeviceRepository.class);
    }

    @Override
    protected UserDevice createModel() throws ExecutionException, InterruptedException {
        return this.createUserDevice();
    }

    @Override
    protected UserDevice updateModel(UserDevice created) throws ExecutionException, InterruptedException {
        created.setDeviceToken("newDeviceToken");
        return this.sut.update(created).toCompletableFuture().get();
    }

    @Override
    protected Boolean checkModelUpdate(UserDevice original, UserDevice updated) {
        return (original.getDeviceToken().equals(updated.getDeviceToken()));
    }

    @Override
    protected LinkedHashMap<String, String> getExistingField() {
        LinkedHashMap<String, String> stringStringHashMap = new LinkedHashMap<>();
        stringStringHashMap.put("deviceToken", "deviceToken");
        return stringStringHashMap;
    }

    @Test
    public void test_getUserDevices() throws ExecutionException, InterruptedException {
        User user = this.createUser("name", "usernameToFindInDB", "password", UserProfileEnum.ADMIN);
        User user2 = this.createUser("name2", "usernameToFindInDB2", "password", UserProfileEnum.ADMIN);
        this.createUserDevice(user2, "deviceToken1", "", "");
        UserDevice userDevice2 = this.createUserDevice(user, "deviceToken2", "", "");


        List<UserDevice> actual = this.sut.getUserDevices(user).toCompletableFuture().get();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getDeviceToken()).isEqualTo(userDevice2.getDeviceToken());
    }
}
