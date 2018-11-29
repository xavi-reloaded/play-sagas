package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eric on 7/8/16.
 */
@Entity
@Table(name = "user_device")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class UserDevice extends BaseModel {

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "device_os")
    private String deviceOs;

    @Column(name = "os_version")
    private String osVersion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

}
