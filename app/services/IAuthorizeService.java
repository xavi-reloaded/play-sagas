package services;

import dtos.SessionDTO;
import exceptions.NotPossibleValidateTokenException;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public interface IAuthorizeService {
    CompletionStage<SessionDTO> login(String username, String password);

    String generateNewTokenFromRefreshToken(String refreshToken) throws InterruptedException, ExecutionException, NotPossibleValidateTokenException, IOException;
}
