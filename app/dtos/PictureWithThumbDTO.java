package dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PictureWithThumbDTO {

    String large;
    String thumbnail;

    public PictureWithThumbDTO(String large, String thumbnail){
        this.setLarge(large);
        this.setThumbnail(thumbnail);
    }
}

