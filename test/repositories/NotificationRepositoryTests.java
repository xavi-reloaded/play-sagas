package repositories;

import enumerations.NotificationAction;
import enumerations.NotificationDomain;
import enumerations.UserProfileEnum;
import models.Notification;
import org.junit.Before;
import repository.NotificationRepository;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

public class NotificationRepositoryTests extends BaseCrudRepositoriesTest<Notification, NotificationRepository> {

    @Before
    public void setUp(){
        this.sut = app.injector().instanceOf(NotificationRepository.class);
    }

    @Override
    public Notification createModel() throws ExecutionException, InterruptedException {
        return this.createNotification(this.createUser("name", "username", "1234", UserProfileEnum.ADMIN), NotificationAction.NONE,
                "message", NotificationDomain.NONE, "redId", "title");
    }

    @Override
    protected Notification updateModel(Notification created) throws ExecutionException, InterruptedException {
        created.setTitle("notititle");
        return this.sut.update(created).toCompletableFuture().get();
    }

    @Override
    protected Boolean checkModelUpdate(Notification original, Notification updated) {
        return (original.getTitle().equals(updated.getTitle()));
    }

    @Override
    protected LinkedHashMap<String, String> getExistingField() {
        LinkedHashMap<String, String> stringStringHashMap = new LinkedHashMap<>();
        stringStringHashMap.put("title", "title");
        return stringStringHashMap;
    }
}
