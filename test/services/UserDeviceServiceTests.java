package services;

import models.User;
import models.UserDevice;
import org.junit.Before;
import org.junit.Test;
import repository.UserDeviceRepository;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserDeviceServiceTests extends BaseServiceTests<UserDevice, UserDeviceService, UserDeviceRepository> {


    public UserDeviceServiceTests() {
        super(UserDevice.class, UserDeviceRepository.class);
    }

    @Before
    public void setUp() {
        this.sut = new UserDeviceService(this.repositoryMock);
    }

    @Override
    UserDevice getModel() {
        UserDevice userDevice = new UserDevice();
        userDevice.setId(1L);
        userDevice.setDeviceOs("android");
        return userDevice;
    }

    @Test
    public void test_getUserDevices_ok() throws ExecutionException, InterruptedException {
        when(this.repositoryMock.getUserDevices(any())).thenReturn(supplyAsync(() -> Arrays.asList(new UserDevice(), new UserDevice())));
        List<UserDevice> actual = this.sut.getUserDevices(new User()).toCompletableFuture().get();
        assertThat(actual.size()).isEqualTo(2);
    }


}
