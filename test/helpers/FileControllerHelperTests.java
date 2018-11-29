package helpers;

import exceptions.CouldNotCompletelyReadFileException;
import exceptions.CouldNotCompletelyReadFileItIsTooLongException;
import exceptions.NotPossibleSaveFileException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileControllerHelperTests {

    @Test
    public void test_readBytesFromFile_ok() throws NotPossibleSaveFileException, CouldNotCompletelyReadFileException, CouldNotCompletelyReadFileItIsTooLongException {
        byte[] bytesToCreateNewFile = {0, 1, 2, 3, 4};
        byte[] readBytes;

        File existingFile = new File("data/tmp/test.txt");
        FileControllerHelper.writeBytesToFile(existingFile, bytesToCreateNewFile);
        readBytes = FileControllerHelper.readBytesFromFile(existingFile);

        Assert.assertArrayEquals(bytesToCreateNewFile, readBytes);
    }

    @Test(expected = CouldNotCompletelyReadFileException.class)
    public void test_readBytesFromFile_incorrectFile() throws CouldNotCompletelyReadFileException, CouldNotCompletelyReadFileItIsTooLongException {
        File fileToRead = new File("data/tmp/testIncorrecFile.txt");
        FileControllerHelper.readBytesFromFile(fileToRead);
    }

    @Test
    public void test_writeBytesToFile_ok() throws NotPossibleSaveFileException, CouldNotCompletelyReadFileException, CouldNotCompletelyReadFileItIsTooLongException {
        byte[] bytes = {0, 1, 2, 3, 4, 5};
        byte[] readBytes;

        File fileToCreate = new File("data/tmp/test_writeBytesToFile_ok.txt");
        FileControllerHelper.writeBytesToFile(fileToCreate, bytes);
        readBytes = FileControllerHelper.readBytesFromFile(fileToCreate);

        Assert.assertArrayEquals(bytes, readBytes);
    }

    @Test(expected = NotPossibleSaveFileException.class)
    public void test_writeBytesToFile_fileNotExist() throws NotPossibleSaveFileException {
        byte[] bytes = {0, 1, 2, 3, 4, 5};
        File theFile = new File("/incorrectDirectory/test.txt");
        FileControllerHelper.writeBytesToFile(theFile, bytes);
    }
}
