package adapters;

import dtos.FileStream;
import exceptions.AmazonException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface IS3Adapter {
    Boolean uploadFile(File file, String keyName, Boolean publicFile) throws AmazonException;

    Boolean uploadBufferedImage(BufferedImage image, String keyName) throws IOException;

    Boolean deleteFile(String keyName);

    FileStream getFile(String key);

    void createFile(String fileKey, String fileContent);
}
