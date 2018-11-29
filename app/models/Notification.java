package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import enumerations.NotificationAction;
import enumerations.NotificationDomain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by eric on 2/19/16.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Notification extends BaseModel {

    @Column(name="title", columnDefinition = "TEXT")
    public String title;

    @Column(name="message", columnDefinition = "TEXT")
    public String message;

    @Column(name="referenced_domain")
    public NotificationDomain referencedDomain;

    @Column(name="action")
    public NotificationAction action;

    @Column(name="reference_id")
    public String referencedId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="viewed")
    public Date viewed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="opened")
    public Date opened;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    public User user;

    public Notification(){
        // Empty constructor
    }
}
