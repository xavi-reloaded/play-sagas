package helpers;

import dtos.FileDTO;
import exceptions.*;
import play.mvc.Http;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public interface IFileHelper {
    String saveFileToTmp(Http.MultipartFormData.FilePart partFile) throws NotPossibleCreateDirException, NotPossibleSaveFileException;

    String saveAndCropImage(String tmpFileName, Integer width, Integer height) throws NotPossibleCreateDirException, CouldNotCompletelyReadFileItIsTooLongException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, NotPossibleCropImgException, AmazonException;

    String saveImage(String tmpFileName, Integer width, Integer height) throws NotPossibleCreateDirException, CouldNotCompletelyReadFileItIsTooLongException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, NotPossibleCropImgException, AmazonException;

    String saveFile(String tmpFileName, Boolean publicFile) throws CouldNotCompletelyReadFileException, NotPossibleCreateDirException, NotPossibleSaveFileException, CouldNotCompletelyReadFileItIsTooLongException, AmazonException;

    FileDTO getFile(String tmpFileName) throws FileNotExistException;

    FileDTO getFile(File localFile) throws FileNotExistException;

    Boolean isTmpFile(String url);

    boolean removeFile(String url);

    boolean removeFile(File file);

    Optional<String> downloadImage(URL sourceUrl);
}
