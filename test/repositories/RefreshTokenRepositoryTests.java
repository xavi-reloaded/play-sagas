package repositories;

import enumerations.UserProfileEnum;
import models.RefreshToken;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.RefreshTokenRepository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class RefreshTokenRepositoryTests extends RepositoryExercises {

    private RefreshTokenRepository sut;

    @Before
    public void setUp(){
        this.sut = app.injector().instanceOf(RefreshTokenRepository.class);
    }

    @Test
    public void test_insert() throws ExecutionException, InterruptedException {
        User user = this.createUser("name", "usename", "1234", UserProfileEnum.ADMIN);
        RefreshToken thisisthetoken = this.createRefreshToken(user, "thisisthetoken");
        Assert.assertTrue(thisisthetoken.getId()>0);
    }

    @Test
    public void test_lookup() throws ExecutionException, InterruptedException {
        User user = this.createUser("name", "usename", "1234", UserProfileEnum.ADMIN);
        RefreshToken thisisthetoken = this.createRefreshToken(user, "thisisthetoken");
        CompletionStage<Optional<RefreshToken>> lookup = this.sut.lookup(thisisthetoken.getId());
        await().atMost(1, SECONDS).until(() ->
                assertThat(lookup.toCompletableFuture()).isCompletedWithValueMatching(refre -> {
                    final RefreshToken refreshToken = refre.get();
                    return (refreshToken.getToken().equals("thisisthetoken"));
                })
        );
    }

    @Test
    public void test_update() throws ExecutionException, InterruptedException {
        User user = this.createUser("name", "usename", "1234", UserProfileEnum.ADMIN);
        RefreshToken thisisthetoken = this.createRefreshToken(user, "thisisthetoken");
        thisisthetoken.setToken("newtokenstring");
        thisisthetoken = this.sut.update(thisisthetoken).toCompletableFuture().get();
        CompletionStage<Optional<RefreshToken>> lookup = this.sut.lookup(thisisthetoken.getId());
        await().atMost(1, SECONDS).until(() ->
                assertThat(lookup.toCompletableFuture()).isCompletedWithValueMatching(refre -> {
                    final RefreshToken refreshToken = refre.get();
                    return (refreshToken.getToken().equals("newtokenstring"));
                })
        );
    }
}
