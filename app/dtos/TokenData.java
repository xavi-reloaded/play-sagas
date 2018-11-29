package dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import models.User;
import play.libs.Json;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenData {
    User user;

    public TokenData(User user){
        this.user = user;
    }

    public TokenData(){}

    public String toString(){
        return Json.toJson(this).toString();
    }
}
