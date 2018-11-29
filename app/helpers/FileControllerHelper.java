package helpers;

import exceptions.CouldNotCompletelyReadFileException;
import exceptions.CouldNotCompletelyReadFileItIsTooLongException;
import exceptions.NotPossibleSaveFileException;

import java.io.*;

public class FileControllerHelper {

    private FileControllerHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] readBytesFromFile(File file) throws CouldNotCompletelyReadFileException, CouldNotCompletelyReadFileItIsTooLongException {

        try (InputStream inputStream = new FileInputStream(file)) {
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new CouldNotCompletelyReadFileItIsTooLongException();
            }

            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead;
            while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new CouldNotCompletelyReadFileException();
            }

            return bytes;
        } catch (IOException e) {
            throw new CouldNotCompletelyReadFileException();
        }
    }

    public static void writeBytesToFile(File theFile, byte[] bytes) throws NotPossibleSaveFileException {
        try (FileOutputStream fos = new FileOutputStream(theFile); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos)) {
            bufferedOutputStream.write(bytes);
        } catch (IOException e) {
            throw new NotPossibleSaveFileException();
        }
    }
}
