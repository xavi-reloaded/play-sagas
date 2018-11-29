package services;

import com.google.inject.Inject;
import dtos.RefreshTokenData;
import dtos.SessionDTO;
import dtos.TokenData;
import exceptions.NotPossibleValidateTokenException;
import exceptions.WrongLoginException;
import models.User;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class AuthorizeService implements IAuthorizeService {

    private IUserService iUserService;
    private TokenService tokenService;

    @Inject
    public AuthorizeService(UserService userService, TokenService tokenService) {
        this.iUserService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public CompletionStage<SessionDTO> login(String username, String password){
        return this.iUserService.findByUsername(username).thenApplyAsync(
                userAccountOptional -> {
                    if (!userAccountOptional.isPresent() || !userAccountOptional.get().checkPassword(password)) {
                        throw new CompletionException(new WrongLoginException());
                    } else {
                        User userAccount = userAccountOptional.get();
                        String token =  this.tokenService.generateToken(new TokenData(userAccount));
                        String refreshToken =  this.tokenService.generateRefreshToken(new TokenData(userAccount));
                        return new SessionDTO(userAccount, token, refreshToken);
                    }
                }
        );
    }

    @Override
    public String generateNewTokenFromRefreshToken(String refreshToken) throws InterruptedException, ExecutionException, NotPossibleValidateTokenException, IOException {
        RefreshTokenData refreshTokenData = this.tokenService.verifyRefreshTokenAndGetData(refreshToken);
        CompletableFuture<Optional<User>> userCompletateFuture = this.iUserService.lookup(refreshTokenData.getUser().getId()).toCompletableFuture();
        return this.tokenService.generateNewTokenFromRefreshToken(userCompletateFuture.get().get());
    }

}
