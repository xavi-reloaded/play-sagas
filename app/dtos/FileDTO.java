package dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FileDTO {

    File file;
    String contentType;

    public FileDTO(String url, String contentType){
        this.setFile(new File(url));
        this.setContentType(contentType);
    }
}

