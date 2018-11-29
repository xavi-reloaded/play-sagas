package services;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import dtos.FileDTO;
import dtos.PictureWithThumbDTO;
import exceptions.*;
import helpers.FileHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import play.mvc.Http;

import java.io.File;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class AssetServiceTests {

    private AssetService sut;
    private FileHelper fileHelperMock;

    @Before
    public void setUp() throws NotPossibleCreateDirException, NotPossibleSaveFileException, AmazonException, FileNotExistException, NotPossibleCropImgException {
        this.fileHelperMock = mock(FileHelper.class);

        when(fileHelperMock.saveFileToTmp(any(Http.MultipartFormData.FilePart.class))).thenAnswer(invocation -> {
            Http.MultipartFormData.FilePart file = invocation.getArgument(0);
            return file.getFilename();
        });
        when(fileHelperMock.saveAndCropImage(any(), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(fileHelperMock.saveImage(any(), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(fileHelperMock.saveFile(any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(fileHelperMock.getFile(anyString())).thenAnswer(invocation -> new FileDTO("", ""));
        when(fileHelperMock.getFile(any(File.class))).thenAnswer(invocation -> null);
        when(fileHelperMock.isTmpFile(anyString())).thenAnswer(invocation -> {
            String  result = invocation.getArgument(0);
            return result.contains("tmpFile");
        });
        when(fileHelperMock.removeFile(anyString())).thenAnswer(invocation -> true);
        when(fileHelperMock.removeFile(any(File.class))).thenAnswer(invocation -> true);


        this.sut = new AssetService(fileHelperMock);
    }

    @Test
    public void tests_uploadFile_ok() throws Exception {
        Http.MultipartFormData.FilePart fileToUpload = new Http.MultipartFormData.FilePart("file", "dummyFile.txt", "", null);
        String actual = this.sut.uploadFile(fileToUpload) ;
        Assert.assertEquals(actual, "dummyFile.txt");
    }

    @Test
    public void tests_commitImage_ok () throws Exception {
        PictureWithThumbDTO actual = this.sut.commitImage("dummyImage.jpg");
        Assert.assertEquals(actual, null);
    }

    @Test
    public void tests_commitImage_ok2 () throws Exception {
        PictureWithThumbDTO actual = this.sut.commitImage("tmpFile/dummyImage.jpg");
        Assert.assertEquals(actual.getLarge(), "tmpFile/dummyImage.jpg");
    }

    @DataProvider
    public static Object[][] commitFileDataProviderAdd() {
        return new Object[][] {
                {"dummyFile.txt", null},
                {"tmpFileDummyFile2.txt", null},
                {"tmpFile/dummyFile.txt", "tmpFile/dummyFile.txt"}
        };
    }

    @Test
    @UseDataProvider("commitFileDataProviderAdd")
    public void tests_commitFile_ok(String commitFile, String result) throws AmazonException {
        when(fileHelperMock.saveFile("tmpFileDummyFile2.txt", false)).thenThrow(new AmazonException(""));

        String actual = this.sut.commitFile(commitFile);
        Assert.assertEquals(actual, result);
    }

    @Test
    public void tests_removeFile_ok() {
        Boolean actual = this.sut.removeFile("dummyFile.txt");
        Assert.assertEquals(actual, true);
    }

    @Test
    public void tests_getFile_ok() throws FileNotExistException {
        when(fileHelperMock.getFile("dummyFile2.txt")).thenThrow(new FileNotExistException(""));
        FileDTO actual = this.sut.getFile("dummyFile.txt");
        Assert.assertNotEquals(actual, null);
    }

    @Test(expected = FileNotExistException.class)
    public void tests_getFile_FileNotExistException() throws FileNotExistException {
        when(fileHelperMock.getFile("dummyFile2.txt")).thenThrow(new FileNotExistException(""));
        this.sut.getFile("dummyFile2.txt");
    }
}