package repositories;

import enumerations.NotificationAction;
import enumerations.NotificationDomain;
import enumerations.UserProfileEnum;
import models.*;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repository.*;
import tasks.TasksModule;

import java.util.concurrent.ExecutionException;

abstract class RepositoryExercises  extends WithApplication {

    @Override
    protected Application provideApplication() {
        GuiceApplicationBuilder guiceApplicationBuilder = new GuiceApplicationBuilder();
        guiceApplicationBuilder.disable(TasksModule.class);
        return guiceApplicationBuilder.build();
    }

    public User createUser(String name, String username, String password, UserProfileEnum profile) throws ExecutionException, InterruptedException {
        UserRepository userRepository = app.injector().instanceOf(UserRepository.class);
        User tocreate = new User();
        tocreate.setName(name);
        tocreate.setPassword(password);
        tocreate.setUsername(username);
        tocreate.setProfile(profile);
        return userRepository.insert(tocreate).toCompletableFuture().get();
    }

    public RefreshToken createRefreshToken(User user, String token) throws ExecutionException, InterruptedException {
        RefreshTokenRepository refreshTokenRepository = app.injector().instanceOf(RefreshTokenRepository.class);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        return refreshTokenRepository.insert(refreshToken).toCompletableFuture().get();
    }


    public Notification createNotification(User user, NotificationAction action, String message, NotificationDomain referencedDomain, String referencedId, String title) throws ExecutionException, InterruptedException {
        NotificationRepository repository = app.injector().instanceOf(NotificationRepository.class);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setAction(action);
        notification.setMessage(message);
        notification.setReferencedDomain(referencedDomain);
        notification.setReferencedId(referencedId);
        notification.setTitle(title);
        return repository.insert(notification).toCompletableFuture().get();
    }

    public UserDevice createUserDevice() throws ExecutionException, InterruptedException {
        return this.createUserDevice(this.createUser("name", "username", "1234", UserProfileEnum.ADMIN), "deviceTokenTest", "deviceOsTest", "osVersionTest");
    }

    public UserDevice createUserDevice(User user, String deviceToken, String deviceOs, String osVersion) throws ExecutionException, InterruptedException {
        UserDeviceRepository repository = app.injector().instanceOf(UserDeviceRepository.class);
        UserDevice entity = new UserDevice();
        entity.setUser(user);
        entity.setDeviceToken(deviceToken);
        entity.setDeviceOs(deviceOs);
        entity.setOsVersion(osVersion);
        return repository.insert(entity).toCompletableFuture().get();
    }
}
