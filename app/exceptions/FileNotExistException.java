package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class FileNotExistException extends BaseAppException {

    public FileNotExistException(String fileName){
        super(HTTPStatusCodes.NOT_FOUND, ErrorCodes.FILE_NOT_EXIST , FileNotExistException.buildMessage(fileName));
    }

    private static String buildMessage(String fileName){
        return  "The file ("+fileName+") does not exist.";
    }
}
