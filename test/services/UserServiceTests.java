package services;

import dtos.PagedListDTO;
import exceptions.*;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.core.Execution;
import play.libs.concurrent.HttpExecutionContext;
import repository.UserRepository;


import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTests extends WithPictureServiceTests<User, UserService, UserRepository> {

    public UserServiceTests() throws AmazonException, NotPossibleCreateDirException, NotPossibleCropImgException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, CouldNotCompletelyReadFileItIsTooLongException {
        super(User.class, UserRepository.class);
    }

    @Before
    public void setUp() {
        when(this.repositoryMock.findByUsername(anyString())).thenAnswer(invocation -> supplyAsync(() -> {
            String username = invocation.getArgument(0);
            if (username.equals("existing")) {
                User value = new User();
                value.setUsername(username);
                return Optional.of(value);
            } else {
                return Optional.empty();
            }
        }));
        HttpExecutionContext ctx = new HttpExecutionContext(Execution.internalContext());

        this.sut = new UserService(this.repositoryMock, this.assetServiceMock, ctx);
    }

    @Test
    public void test_findByUsername() throws ExecutionException, InterruptedException {
        CompletionStage<Optional<User>> userOptionalFuture = this.sut.findByUsername("existing");
        Optional<User> actual = userOptionalFuture.toCompletableFuture().get();
        assertThat(actual.get().getUsername()).isEqualTo("existing");
    }

    @Test
    public void test_findAll() throws ExecutionException, InterruptedException {
        PagedListDTO<User> actual = this.sut.findAll(0, 10, "any", "any", "any").toCompletableFuture().get();
        assertThat(actual.getData().size()).isEqualTo(1);
    }

    @Test
    public void test_createUser() throws ExecutionException, InterruptedException {
        User user = new User();
        user.setUsername("notExistst");
        User actual = this.sut.createUser(user, "password").toCompletableFuture().get();
        assertThat(actual.getId()).isEqualTo(101011L);
    }

    @Test
    public void test_createUser_exception() throws InterruptedException {
        User user = new User();
        user.setUsername("notExistst");
        when(repositoryMock.insert(any())).thenAnswer(invocation -> supplyAsync(() -> {
            throw new CompletionException(new InterruptedException());
        }));

        String actual = "";
        try {
            this.sut.createUser(user, "password").toCompletableFuture().get();
        } catch (ExecutionException e) {
            actual = e.getMessage();
        }

        assertThat(actual).isEqualTo("java.util.concurrent.ExecutionException: java.lang.InterruptedException");
    }

    @Test
    public void test_createUser_repeatedUsername() throws InterruptedException {
        User user = new User();
        user.setUsername("existing");
        String actual = "";
        try {
            this.sut.createUser(user, "password").toCompletableFuture().get();
        } catch (ExecutionException e) {
            actual = e.getMessage();
        }

        assertThat(actual).isEqualTo("exceptions.UsernameTakenException: Username taken");
    }
}