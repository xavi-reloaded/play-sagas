package services;

import adapters.JWTAdapter;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dtos.RefreshTokenData;
import dtos.TokenData;
import exceptions.NotPossibleValidateTokenException;
import helpers.ConfigHelper;
import helpers.IConfigHelper;
import models.RefreshToken;
import models.User;
import play.Logger;
import repository.RefreshTokenRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Singleton
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private IConfigHelper configHelper;
    private static final String TOKEN_DATA_KEY = "tokenData";
    private String tokenIssuer;
    private String tokenRefreshIssuer;
    private String secret;
    private JWTAdapter tokenGeneratorAdapter;

    @Inject
    public TokenService(ConfigHelper configHelper, RefreshTokenRepository refreshTokenRepository, JWTAdapter tokenGeneratorAdapter) {
        this.configHelper = configHelper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenGeneratorAdapter = (tokenGeneratorAdapter != null) ? tokenGeneratorAdapter : new JWTAdapter();
        initParams();
    }

    private void initParams() {
        this.tokenIssuer = this.configHelper.getConfigString(ConfigHelper.APP_NAME);
        this.tokenRefreshIssuer = this.tokenIssuer + "rtoken";
        this.secret = this.configHelper.getConfigString(ConfigHelper.SECRET_KEY);
    }

    public String generateToken(TokenData tokenData) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(Long.parseLong(this.configHelper.getConfigString(ConfigHelper.TOKEN_EXPIRY_MINUTES)));
        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());
        return this.tokenGeneratorAdapter.getToken(tokenData.toString(), expiryDate, this.secret, this.tokenIssuer, TOKEN_DATA_KEY);
    }


    public String generateRefreshToken(TokenData tokenData) {
        try {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(tokenData.getUser());
            refreshToken = this.refreshTokenRepository.insert(refreshToken).toCompletableFuture().get();
            RefreshTokenData refreshTokenData = new RefreshTokenData();
            refreshTokenData.setUser(tokenData.getUser());
            refreshTokenData.setRefreshTokenId(refreshToken.getId());
            String token = this.tokenGeneratorAdapter.getRefreshToken(refreshToken, refreshTokenData, this.secret, this.tokenRefreshIssuer, TOKEN_DATA_KEY);
            this.refreshTokenRepository.update(refreshToken);
            return token;
        } catch (JWTCreationException | InterruptedException | ExecutionException e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    public TokenData verifyTokenAndGetData(String token) throws IOException {
        String tokenData = this.tokenGeneratorAdapter.verifyTokenAndGetData(token, this.tokenIssuer, this.secret, TOKEN_DATA_KEY);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(tokenData, TokenData.class);
    }

    public RefreshTokenData verifyRefreshTokenAndGetData(String refreshToken) throws IOException, NotPossibleValidateTokenException, ExecutionException, InterruptedException {
        String tokenData = this.tokenGeneratorAdapter.verifyTokenAndGetData(refreshToken, this.tokenRefreshIssuer, this.secret, TOKEN_DATA_KEY);
        ObjectMapper mapper = new ObjectMapper();
        RefreshTokenData refreshTokenData = mapper.readValue(tokenData, RefreshTokenData.class);
        Optional<RefreshToken> refreshTokenOptional = this.refreshTokenRepository.lookup(refreshTokenData.getRefreshTokenId()).toCompletableFuture().get();
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshTokenStored = refreshTokenOptional.get();
            if (refreshTokenStored.getToken().equals(refreshToken) && refreshTokenStored.active) {
                return refreshTokenData;
            }
        }
        throw new NotPossibleValidateTokenException();
    }

    public String generateNewTokenFromRefreshToken(User user) {
        return this.generateToken(new TokenData(user));
    }
}
