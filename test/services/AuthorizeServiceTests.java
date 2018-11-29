package services;

import dtos.RefreshTokenData;
import dtos.SessionDTO;
import exceptions.NotPossibleValidateTokenException;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthorizeServiceTests {

    private AuthorizeService sut;
    @Mock
    private TokenService tokenServiceMock;

    @Mock
    private UserService userServiceMock;

    @Before
    public void setUp() throws InterruptedException, ExecutionException, NotPossibleValidateTokenException, IOException {
        RefreshTokenData refreshTokenData = new RefreshTokenData();
        refreshTokenData.setUser(new User());


        MockitoAnnotations.initMocks(this);
        when(tokenServiceMock.generateToken(any())).thenReturn("token");
        when(tokenServiceMock.generateRefreshToken(any())).thenReturn("refreshtoken");
        when(tokenServiceMock.generateNewTokenFromRefreshToken(any())).thenReturn("newtoken");
        when(tokenServiceMock.verifyRefreshTokenAndGetData(any())).thenReturn(refreshTokenData);

        when(userServiceMock.lookup(any())).thenReturn(supplyAsync(() -> Optional.of(new User())));
        when(userServiceMock.findByUsername(any())).thenAnswer(invocation -> supplyAsync(() -> {
            String username = invocation.getArgument(0);
            if (username.equals("exist")) {
                User user = new User();
                user.setPassword("1234");
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        }));

        this.sut = new AuthorizeService(this.userServiceMock, tokenServiceMock);
    }

    @Test
    public void test_login_ok() throws ExecutionException, InterruptedException {
        SessionDTO actual = this.sut.login("exist", "1234").toCompletableFuture().get();
        Assert.assertEquals("refreshtoken", actual.getRefreshToken());
    }

    @Test
    public void test_login_incorrectpassword() throws InterruptedException {
        String actual = "";
        try {
            this.sut.login("exist", "1234567").toCompletableFuture().get();
        } catch (ExecutionException e) {
            actual = e.getMessage();
        }
        Assert.assertEquals("exceptions.WrongLoginException: Email or password is wrong.", actual);
    }

    @Test
    public void test_login_incorrectusername() throws InterruptedException {
        String actual = "";
        try {
            this.sut.login("notexist", "1234567").toCompletableFuture().get();
        } catch (ExecutionException e) {
            actual = e.getMessage();
        }
        Assert.assertEquals("exceptions.WrongLoginException: Email or password is wrong.", actual);
    }

    @Test
    public void test_generateNewTokenFromRefreshToken() throws Exception {
        String actual = this.sut.generateNewTokenFromRefreshToken("refreshtoken");
        Assert.assertEquals("newtoken", actual);
    }
}