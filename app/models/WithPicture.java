package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * Created by eko327 on 18/7/17.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class WithPicture extends BaseModel{
    public abstract String getPicture();

    public abstract String getThumbnail();

    public abstract void setPicture(String picture);

    public abstract void setThumbnail(String thumbnail);
}
