package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Model;
import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.UpdatedTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseModel extends Model {
    @Id
    public Long id;

    @CreatedTimestamp
    @JsonFormat
    private Date created;

    @JsonIgnore
    @UpdatedTimestamp
    @JsonFormat
    private Date updated;

    @Column
    private Long createdBy;

    @Column
    private Long updatedBy;

}
