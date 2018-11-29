package helpers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dtos.TokenData;
import models.User;
import play.mvc.Http;
import services.TokenService;

import java.io.IOException;

@Singleton
public class ContextHelper {

    protected final TokenService tokenService;

    @Inject
    public ContextHelper(TokenService tokenService){
        this.tokenService = tokenService;
    }

    public Object get(String key){
        return StaticContextHelper.get(key);
    }

    public void put(String key, Object value){
        StaticContextHelper.put(key, value);
    }

    public void setToken(String token){
        setCurrentToken(token);
    }

    public void setCurrentToken(String token){
        StaticContextHelper.setCurrentToken(token);
    }

    public String getCurrentToken(){
        return (String) get(StaticContextHelper.CURRENT_TOKEN);
    }

    public User getCurrentUser() throws IOException {
        return this.getCurrentUser(this.getCurrentToken());
    }

    public User getCurrentUser(String token) throws IOException {
        TokenData tokenData = this.tokenService.verifyTokenAndGetData(token);
        return tokenData.getUser();
    }

    public Http.RequestHeader getRequestHeader(){
        return Http.Context.current().request();
    }
}

