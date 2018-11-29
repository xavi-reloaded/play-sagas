package adapters;

import dtos.RefreshTokenData;
import models.RefreshToken;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public interface ITokenGenerator {
    String getToken(String tokenData, Date expiryDate, String secret, String tokenIssuer, String tokenDataKey);

    String getRefreshToken(RefreshToken refreshToken, RefreshTokenData refreshTokenData, String secret, String tokenIssuer, String tokenDataKey);

    String verifyTokenAndGetData(String token, String issuer, String secret, String tokenDataKey) throws UnsupportedEncodingException;
}
