package dtos;

import lombok.Getter;
import lombok.Setter;
import models.User;

@Getter
@Setter
public class SessionDTO {
    User user;
    String token;
    String refreshToken;

    public SessionDTO(User userAccount, String token, String refreshToken){
        this.setUser(userAccount);
        this.setToken(token);
        this.setRefreshToken(refreshToken);
    }

    public SessionDTO(){

    }
}
