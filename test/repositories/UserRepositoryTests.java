package repositories;

import enumerations.UserProfileEnum;
import models.User;
import org.junit.Before;
import org.junit.Test;
import repository.UserRepository;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class UserRepositoryTests extends BaseCrudRepositoriesTest<User, UserRepository>{

    @Before
    public void setUp(){
        this.sut = app.injector().instanceOf(UserRepository.class);
    }

    @Test
    public void test_findByUsername() throws ExecutionException, InterruptedException {
        User created = this.createUser("name", "usernameToFindInDB", "password", UserProfileEnum.ADMIN);
        CompletionStage<Optional<User>> usernameToFindInDB = this.sut.findByUsername("usernameToFindInDB");
        await().atMost(1, SECONDS).until(() ->
                assertThat(usernameToFindInDB.toCompletableFuture()).isCompletedWithValueMatching(user -> (user.get().getId().equals(created.getId())))
        );
    }

    @Override
    protected User createModel() throws ExecutionException, InterruptedException {
        return this.createUser("name", "username", "password", UserProfileEnum.ADMIN);
    }

    @Override
    protected User updateModel(User created) throws ExecutionException, InterruptedException {
        created.setUsername("newUsername");
        return this.sut.update(created).toCompletableFuture().get();
    }

    @Override
    protected Boolean checkModelUpdate(User original, User updated) {
        return (original.getUsername().equals(updated.getUsername()));
    }

    @Override
    protected LinkedHashMap<String, String> getExistingField() {
        LinkedHashMap<String, String> stringStringHashMap = new LinkedHashMap<>();
        stringStringHashMap.put("username", "username");
        return stringStringHashMap;
    }
}
