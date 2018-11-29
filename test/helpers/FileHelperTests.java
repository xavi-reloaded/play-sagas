package helpers;

import adapters.S3Adapter;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import dtos.FileDTO;
import exceptions.AmazonException;
import exceptions.FileNotExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import play.Logger;
import play.mvc.Http;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class FileHelperTests {

    private FileHelper sut;

    @DataProvider
    public static Object[][] isTmpFileDataProviderAdd() {
        return new Object[][]{
                {"data/tmp/dummyFile.txt", true},
                {"tmpFile/dummyFile.txt", true},
                {"dummyFile.txt", false}
        };
    }

    @DataProvider
    public static Object[] getFileDataProviderAdd() {
        return new Object[]{
                "data/tmp/",
                "data/files/"
        };
    }

    @Before
    public void setUp() throws AmazonException {
        S3Adapter s3AdapterMock = mock(S3Adapter.class);
        when(s3AdapterMock.uploadFile(any(), any(), any())).thenAnswer(invocation -> {
            File file = invocation.getArgument(0);
            if (file.exists()) {
                return true;
            }
            throw new AmazonException("");
        });
        when(s3AdapterMock.deleteFile(any())).thenReturn(true);

        ConfigHelper configHelperMock = mock(ConfigHelper.class);

        when(configHelperMock.getConfigString(ConfigHelper.APP_TMP_FOLDER)).thenReturn("data/tmp/");
        when(configHelperMock.getConfigString(ConfigHelper.APP_FILES_FOLDER)).thenReturn("data/files/");


        this.sut = new FileHelper(s3AdapterMock, configHelperMock);
    }

    @Test
    public void test_saveFileToTmp_ok() throws Exception {
        Http.MultipartFormData.FilePart file = new Http.MultipartFormData.FilePart("key", "dummyFile", "", createFile(this.sut.tmpDataFolder));
        String savedFileName = this.sut.saveFileToTmp(file);
        File actual = new File(this.sut.tmpDataFolder + savedFileName);
        assertThat(actual.exists()).isEqualTo(true);
    }

    @Test(expected = Exception.class)
    public void test_saveFileToTmp_ko() throws Exception {
        File fileToCreate = new File(generateTmpFileName());
        Http.MultipartFormData.FilePart file = new Http.MultipartFormData.FilePart("key", "dummyFile", "", fileToCreate);
        this.sut.saveFileToTmp(file);
    }

    @Test
    public void test_saveAndCropImage_ok() throws Exception {
        File ImageToUpload = createImageFile(this.sut.tmpDataFolder);
        String actual = this.sut.saveAndCropImage(ImageToUpload.getName(), 10, 10);
        assertThat(actual).isNotEqualTo("");
    }

    @Test
    public void test_saveAndCropImage_fileIsNottmp_ok() throws Exception {
        File expected = new File(generateTmpFileName());
        String actual = this.sut.saveAndCropImage(expected.getName(), 10, 10);
        assertThat(actual).isEqualTo(expected.getName());
    }

    @Test
    public void test_saveImage_ok() throws Exception {
        File imageToUpload = createImageFile(this.sut.dataFolder);
        String actual = this.sut.saveImage(imageToUpload.getName(), 10, 10);
        assertThat(actual).isNotEqualTo("");
    }

    @Test
    public void test_saveImage_fileIsNotTmp_ok() throws Exception {
        File imageToUpload = new File(generateTmpFileName());
        String actual = this.sut.saveImage(imageToUpload.getName(), 10, 10);
        assertThat(actual).isEqualTo(imageToUpload.getName());
    }

    @Test
    public void test_saveFile_ok() throws AmazonException {
        File fileToCreate = createFile(this.sut.tmpDataFolder);
        String actual = this.sut.saveFile(fileToCreate.getName(), false);
        assertThat(actual).isNotEqualTo("");
    }

    @Test(expected = AmazonException.class)
    public void test_saveFile_IncorrectFile() throws AmazonException {
        this.sut.saveFile(generateTmpFileName(), false);
    }

    @Test
    @UseDataProvider("getFileDataProviderAdd")
    public void test_getFile_ok(String directoryFile) throws FileNotExistException {
        File dummyFile = createFile(directoryFile);
        FileDTO actual = this.sut.getFile(dummyFile.getName());
        assertThat(actual.getFile().exists()).isEqualTo(true);
    }

    @Test
    @UseDataProvider("getFileDataProviderAdd")
    public void test_getFile_byFile_Ok(String directoryFile) throws FileNotExistException {
        File dummyFile = createFile(directoryFile);
        FileDTO actual = this.sut.getFile(dummyFile);
        assertThat(actual.getFile().exists()).isEqualTo(true);
    }

    @Test(expected = Exception.class)
    public void test_getFile_fileNotExist() throws FileNotExistException {
        this.sut.getFile(generateTmpFileName());
    }

    @Test(expected = FileNotExistException.class)
    public void test_getFile_byFile_fileNotExist() throws FileNotExistException {
        this.sut.getFile(generateTmpFileName());
    }

    @Test
    @UseDataProvider("isTmpFileDataProviderAdd")
    public void test_isTmpFile_ok(String urlFile, boolean expected) {
        boolean actual = this.sut.isTmpFile(urlFile);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void test_removeFile_ok() {
        File dummyFile = createFile(this.sut.dataFolder);
        boolean actual = this.sut.removeFile(dummyFile);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void test_removeFile_byURL_ok() {
        //It's a fake URL.
        boolean actual = this.sut.removeFile("https://s3-eu-west-1.amazonaws.com/##s3bucketname##-assets/07734de816bb471ba02cb119bfbe20e2.png");
        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void test_downloadImage_ok() throws Exception {
        File ImageToDownload = createImageFile(this.sut.tmpDataFolder);
        URL url = new URL("file://" + ImageToDownload.getAbsolutePath());
        Optional<String> actual = this.sut.downloadImage(url);
        assertThat(actual.isPresent()).isEqualTo(true);
    }

    @Test
    public void test_downloadImage_urlNotExist() throws Exception {
        URL url = new URL("https://s3-eu-west-1.amazonaws.com/##s3bucketname##-assets/dummyFile.png");
        Optional<String> actual = this.sut.downloadImage(url);
        assertThat(actual.isPresent()).isEqualTo(false);
    }

    private String generateTmpFileName() {
        return "dummyFile" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    private File createFile(String dataFolder) {
        byte[] bytes = {0, 1, 2, 3, 4, 5};
        File fileToCreate = null;

        try {
            File tempDir = new File(dataFolder);
            if (!tempDir.exists()) {
                if (!tempDir.mkdirs()) {
                    throw new Exception();
                }
            }

            String nameFileToCreate = tempDir.getPath() + File.separator + generateTmpFileName();
            fileToCreate = new File(nameFileToCreate);

            FileControllerHelper.writeBytesToFile(fileToCreate, bytes);
        } catch (Exception e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        return fileToCreate;
    }

    private File createImageFile(String dataFolder) {
        File fileToCreate = null;

        try {
            File tempDir = new File(dataFolder);
            if (!tempDir.exists()) {
                if (!tempDir.mkdirs()) {
                    throw new Exception();
                }
            }
            String tmpFileName = generateTmpFileName();
            fileToCreate = new File(tempDir.getPath() + File.separator + tmpFileName + ".jpg");
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.drawString("Dummy Image File!!!", 10, 20);
            ImageIO.write(image, "jpg", fileToCreate);
        } catch (Exception e) {
            Logger.error(this.getClass().getSimpleName(), e.getMessage());
        }
        return fileToCreate;
    }
}