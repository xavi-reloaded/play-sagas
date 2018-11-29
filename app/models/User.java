package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import enumerations.UserProfileEnum;
import helpers.CryptUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SuppressWarnings("serial")
@JsonIdentityInfo(generator = JSOGGenerator.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Table(name = "user_account")
public class User extends WithPicture {

    @Column
    private String name;

    @Column
    private String email;

    @JsonIgnore
    @Column
    private String password;

    @Column
    private String picture;

    @Column
    private String thumbnail;

    @Column
    private Boolean notificationsEnabled;

    @Column
    private String username;

    @Column
    private Boolean isOn;

    @Column
    private Long phone;

    @Column
    private String phonePrefix;

    @Enumerated(EnumType.STRING)
    public UserProfileEnum profile;

    @Column
    private Integer smsValidationCode;

    @Column
    private String lang;

    public User() {
        // Empty constructor
    }

    @JsonIgnore
    public void setPassword(String password) {
        this.password = this.encrypt(password);
    }

    public void setEmail(String email) {
        this.email = (email != null) ? email.toLowerCase() : null;
    }

    private String encrypt(String originalPassword) {
        return CryptUtils.getStringHash(originalPassword);
    }

    public Boolean checkPassword(String passToCheck) {
        return (this.password != null) && CryptUtils.check(passToCheck, this.password);
    }

    @JsonIgnore
    public Integer getSmsValidationCode() {
        return this.smsValidationCode;
    }

    @JsonIgnore
    public void setSmsValidationCode(Integer validationCode) {
        this.smsValidationCode = validationCode;
    }

    @JsonIgnore
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    @JsonIgnore
    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getPhoneWithMask() {
        if (this.phone != null) {
            String s = this.phone.toString();
            return "XXXXXXX" + s.substring(Math.max(s.length() - 2, 0));
        }
        return null;
    }
}
