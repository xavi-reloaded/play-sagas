package services;

import adapters.ITokenGenerator;
import adapters.JWTAdapter;
import dtos.RefreshTokenData;
import dtos.TokenData;
import exceptions.NotPossibleValidateTokenException;
import helpers.ConfigHelper;
import models.RefreshToken;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.RefreshTokenRepository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TokenServiceTests {

    private TokenService sut;
    @Mock
    private RefreshTokenRepository refreshTokenRepositoryMock;
    @Mock
    private ConfigHelper configHelperMock;
    @Mock
    private JWTAdapter tokenGeneratorMock;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        MockitoAnnotations.initMocks(this);
        when(refreshTokenRepositoryMock.insert(any())).thenReturn(supplyAsync(() -> {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setId(1L);
            return refreshToken;
        }));
        when(refreshTokenRepositoryMock.lookup(anyLong())).thenReturn(supplyAsync(() -> {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken("thisistherefreshtoken");
            refreshToken.setId(1L);
            return Optional.of(refreshToken);
        }));

        when(this.configHelperMock.getConfigString(any())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            return (key.equals("app.token_expiry_minutes")) ? "1" : key;
        });

        when(this.tokenGeneratorMock.getToken(any(), any(), any(), any(), any())).thenReturn("thisisTheToken");
        when(this.tokenGeneratorMock.getRefreshToken(any(), any(), any(), any(), any())).thenReturn("thisistherefreshtoken");
        when(this.tokenGeneratorMock.verifyTokenAndGetData(any(), any(), any(), any())).thenReturn("{\"refreshTokenId\":\"1\",\"user\":{\"@id\":\"1\",\"id\":null,\"name\":null,\"email\":null,\"large\":null,\"pictureThumb\":null,\"notificationsEnabled\":null,\"username\":\"username\",\"isOn\":null,\"profile\":null}}");

        this.sut = new TokenService(this.configHelperMock, refreshTokenRepositoryMock, this.tokenGeneratorMock);
    }

    @Test
    public void test_generateToken() throws Exception {
        User user = new User();
        user.setUsername("username");
        TokenData tokenData = new TokenData(user);
        String token = this.sut.generateToken(tokenData);
        TokenData actual = this.sut.verifyTokenAndGetData(token);
        assertThat(actual.getUser().getUsername()).isEqualTo("username");
    }

    @Test
    public void test_generateRefreshToken() throws Exception {
        TokenData tokenData = new TokenData(new User());
        String refreshToken = this.sut.generateRefreshToken(tokenData);
        RefreshTokenData actual = this.sut.verifyRefreshTokenAndGetData(refreshToken);
        assertThat(actual.getRefreshTokenId()).isEqualTo(Long.valueOf(1));
    }

    @Test
    public void test_generateRefreshToken_null() {
        when(refreshTokenRepositoryMock.insert(any())).thenAnswer(invocation -> supplyAsync(() -> {
            throw new CompletionException(new InterruptedException());
        }));
        TokenData tokenData = new TokenData(new User());
        String actual = this.sut.generateRefreshToken(tokenData);

        assertThat(actual).isNull();
    }

    @Test(expected = NotPossibleValidateTokenException.class)
    public void test_verifyRefreshTokenAndGetData_NotPossibleValidateTokenException() throws InterruptedException, ExecutionException, NotPossibleValidateTokenException, IOException {
        this.sut.verifyRefreshTokenAndGetData("asdasdasd");

    }

    @Test
    public void test_generateNewTokenFromRefreshToken() throws Exception {
        User user2 = new User();
        user2.setUsername("username2");

        when(this.tokenGeneratorMock.getToken(any(), any(), any(), any(), any())).thenReturn("thisisTheToken");
        when(this.tokenGeneratorMock.verifyTokenAndGetData(any(), any(), any(), any())).thenReturn("{\"refreshTokenId\":\"1\",\"user\":{\"@id\":\"1\",\"id\":null,\"name\":null,\"email\":null,\"large\":null,\"pictureThumb\":null,\"notificationsEnabled\":null,\"username\":\"username2\",\"isOn\":null,\"profile\":null}}");

        User user = new User();
        user.setUsername("username");


        String newToken = this.sut.generateNewTokenFromRefreshToken(user2);
        TokenData actual = this.sut.verifyTokenAndGetData(newToken);

        assertThat(actual.getUser().getUsername()).isEqualTo("username2");
    }
}