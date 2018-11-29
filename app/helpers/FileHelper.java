package helpers;

import adapters.IS3Adapter;
import adapters.S3Adapter;
import dtos.FileDTO;
import exceptions.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import play.Logger;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class FileHelper implements IFileHelper {

    String tmpDataFolder;
    String dataFolder;
    private IS3Adapter s3Adapter;

    @Inject
    public FileHelper(S3Adapter s3Adapter, ConfigHelper configHelper) {
        this.tmpDataFolder = configHelper.getConfigString(ConfigHelper.APP_TMP_FOLDER);
        this.dataFolder = configHelper.getConfigString(ConfigHelper.APP_FILES_FOLDER);
        this.s3Adapter = s3Adapter;
    }

    @Override
    public String saveFileToTmp(Http.MultipartFormData.FilePart partFile) throws NotPossibleSaveFileException, NotPossibleCreateDirException {
        File file = (File) partFile.getFile();
        File tempFile = generateReferenceOfTmpFile(partFile.getFilename());
        try {
            FileControllerHelper.writeBytesToFile(tempFile, FileControllerHelper.readBytesFromFile(file));
            return tempFile.getName();
        } catch (CouldNotCompletelyReadFileItIsTooLongException | CouldNotCompletelyReadFileException e) {
            throw new NotPossibleSaveFileException();
        }
    }

    @Override
    public String saveAndCropImage(String tmpFileName, Integer width, Integer height) throws NotPossibleCreateDirException, NotPossibleCropImgException, AmazonException {
        try {
            String result;
            if (isTmpFile(tmpFileName)) {
                File tmpFile = getReferenceOfTmpFile(tmpFileName);
                File destFile = generateReferenceOfLocalFile(tmpFileName);
                Thumbnails.of(tmpFile).size(width, height).crop(Positions.CENTER).toFile(destFile);
                return uploadFile(destFile, true);
            } else {
                result = tmpFileName;
            }
            return result;
        } catch (IOException e) {
            throw new NotPossibleCropImgException();
        }
    }

    @Override
    public String saveImage(String tmpFileName, Integer width, Integer height) throws NotPossibleCreateDirException, AmazonException, NotPossibleCropImgException {
        try {

            String result;
            if (isTmpFile(tmpFileName)) {
                File tmpFile = getReferenceOfTmpFile(tmpFileName);
                File destFile = generateReferenceOfLocalFile(tmpFileName);
                Thumbnails.of(tmpFile).size(width, height).toFile(destFile);
                return uploadFile(destFile, true);
            } else {
                result = tmpFileName;
            }
            return result;
        } catch (IOException e) {
            throw new NotPossibleCropImgException();
        }
    }

    @Override
    public String saveFile(String tmpFileName, Boolean publicFile) throws AmazonException {
        File file = getReferenceOfTmpFile(tmpFileName);
        return uploadFile(file, publicFile, this.generateLocalFileFilename(tmpFileName));
    }

    private String uploadFile(File file, Boolean publicFile, String filename) throws AmazonException {
        s3Adapter.uploadFile(file, filename, publicFile);

        this.removeFile(file);
        return filename;
    }

    private String uploadFile(File destFile, Boolean publicFile) throws AmazonException {
        String filename = destFile.getName();
        return this.uploadFile(destFile, publicFile, filename);
    }

    @Override
    public FileDTO getFile(String tmpFileName) throws FileNotExistException {
        File tmpFile = getReferenceOfTmpFile(tmpFileName);
        File localFile = getReferenceOfLocaleFile(tmpFileName);
        if (localFile.exists()) {
            return getFile(localFile);
        } else if (tmpFile.exists()) {
            return getFile(tmpFile);
        }
        throw new FileNotExistException(tmpFileName);
    }

    @Override
    public FileDTO getFile(File localFile) throws FileNotExistException {
        String contentType = null;
        if (!(localFile.exists() && !localFile.isDirectory())) {
            throw new FileNotExistException(localFile.getName());
        }
        try {
            contentType = localFile.toURI().toURL().openConnection().getContentType();
        } catch (IOException e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }

        return new FileDTO(localFile.getPath(), contentType);
    }

    @Override
    public Boolean isTmpFile(String url) {
        return (url.toLowerCase().contains(this.tmpDataFolder.toLowerCase()) || url.contains("tmpFile"));
    }

    @Override
    public boolean removeFile(String url) {
        return this.s3Adapter.deleteFile(url);
    }

    @Override
    public boolean removeFile(File file) {
        try {
            Files.delete(Paths.get(file.getPath()));
            return true;
        } catch (IOException e) {
            Logger.error(this.getClass().getSimpleName(), "File delete failed " + file.getName());
            return false;
        }
    }

    @Override
    public Optional<String> downloadImage(URL sourceUrl) {
        InputStream in = null;
        try {
            String fileUrl = sourceUrl.toString();
            if (fileUrl.contains("?")) {
                fileUrl = fileUrl.substring(0, sourceUrl.toString().lastIndexOf('?'));
            }
            File tmpFile = generateReferenceOfTmpFile(fileUrl);
            in = sourceUrl.openStream();
            Files.copy(in, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return Optional.of(tmpFile.getName());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.error(e.toString());
                }
            }
        }
    }

    private File generateReferenceOfTmpFile(String fileName) throws NotPossibleCreateDirException {
        String extension = getExtension(fileName);

        File tempDir = new File(this.tmpDataFolder);
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new NotPossibleCreateDirException();
        }
        String tmpFileName = "tmpFile" + UUID.randomUUID().toString().replaceAll("-", "");
        tmpFileName = (extension == null) ? tmpFileName : tmpFileName + "." + extension;
        return new File(tempDir.getPath() + File.separator + tmpFileName);
    }

    private File generateReferenceOfLocalFile(String filePath) throws NotPossibleCreateDirException {
        String fileName = generateLocalFileFilename(filePath);
        String uploadedFileDirs = this.dataFolder;
        if (!(new File(uploadedFileDirs).exists()) && !new File(uploadedFileDirs).mkdirs()) {
            throw new NotPossibleCreateDirException();
        }
        return new File(uploadedFileDirs, fileName);
    }

    private String generateLocalFileFilename(String filePath) {
        String fileName = UUID.randomUUID().toString().replace("-", "");
        fileName += "." + this.getExtension(filePath);
        return fileName;
    }

    private File getReferenceOfTmpFile(String fileName) {
        return new File(this.tmpDataFolder + fileName);
    }

    private File getReferenceOfLocaleFile(String fileName) {
        return new File(this.dataFolder + fileName);
    }

    private String getExtension(String fileName) {
        if (fileName.lastIndexOf('.') != -1 && fileName.lastIndexOf('.') != 0)
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        else return null;
    }
}