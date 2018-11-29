package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SuppressWarnings("serial")
@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RefreshToken extends BaseModel {

    @Column(columnDefinition = "TEXT")
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonIgnore
    public User user;

    @Column
    public Boolean active;

    public RefreshToken(){
        this.active = true;
    }

    public RefreshToken(User userAccount, String token){
        this.setUser(userAccount);
        this.setToken(token);
        this.active = true;
    }

}
