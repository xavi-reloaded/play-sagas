package dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class FileStream {
    InputStream fileInputStream;
    String contentType;

    public FileStream(InputStream fileInputStream, String contentType) {
        this.fileInputStream = fileInputStream;
        this.contentType = contentType;
    }
}
