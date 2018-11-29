package adapters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dtos.RefreshTokenData;
import models.RefreshToken;
import play.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JWTAdapter implements ITokenGenerator {

    @Override
    public String getToken(String tokenData, Date expiryDate, String secret, String tokenIssuer, String tokenDataKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(tokenIssuer)
                    .withClaim(tokenDataKey, tokenData)
                    .withIssuedAt(new Date())
                    .withExpiresAt(expiryDate)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException | JWTCreationException e){
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }


    @Override
    public String getRefreshToken(RefreshToken refreshToken, RefreshTokenData refreshTokenData, String secret, String tokenIssuer, String tokenDataKey) {
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        String token = JWT.create()
                .withIssuer(tokenIssuer)
                .withClaim(tokenDataKey, refreshTokenData.toString())
                .withIssuedAt(new Date())
                .sign(algorithm);
        refreshToken.setToken(token);
        return token;
    }

    @Override
    public String verifyTokenAndGetData(String token, String issuer, String secret, String tokenDataKey) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(tokenDataKey).asString();
    }

}
