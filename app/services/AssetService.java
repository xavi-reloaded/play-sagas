package services;

import dtos.FileDTO;
import dtos.PictureWithThumbDTO;
import exceptions.*;
import helpers.FileHelper;
import helpers.IFileHelper;
import play.Logger;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AssetService {

    private IFileHelper fileHelper;

    @Inject
    public AssetService(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public String uploadFile(Http.MultipartFormData.FilePart fileUpload) throws NotPossibleCreateDirException, NotPossibleSaveFileException {
        return this.fileHelper.saveFileToTmp(fileUpload);
    }

    public PictureWithThumbDTO commitImage(String tmpFilePath) throws CouldNotCompletelyReadFileItIsTooLongException, CouldNotCompletelyReadFileException, NotPossibleCreateDirException, NotPossibleSaveFileException, NotPossibleCropImgException, AmazonException {
        PictureWithThumbDTO result = null;
        if (this.fileHelper.isTmpFile(tmpFilePath)) {
            String large = this.fileHelper.saveImage(tmpFilePath, 1080, 1080);
            String thumbnail = this.fileHelper.saveAndCropImage(tmpFilePath, 350, 350);
            result = new PictureWithThumbDTO(large, thumbnail);
            this.removeFile(tmpFilePath);
        }
        return result;
    }

    public String commitFile(String tmpFilePath) {
        return this.commitFile(tmpFilePath, false);
    }

    public String commitFile(String tmpFilePath, Boolean isPublic) {
        try {
            if (this.fileHelper.isTmpFile(tmpFilePath)) {
                return this.fileHelper.saveFile(tmpFilePath, isPublic);
            }
        } catch (CouldNotCompletelyReadFileItIsTooLongException | CouldNotCompletelyReadFileException | NotPossibleCreateDirException | NotPossibleSaveFileException | AmazonException e) {
            Logger.error(this.getClass().getSimpleName(), e);
        }
        return null;
    }

    public Boolean removeFile(String fileName) {
        return this.fileHelper.removeFile(fileName);
    }

    public FileDTO getFile(String urlFile) throws FileNotExistException {
        return this.fileHelper.getFile(urlFile);
    }
}
