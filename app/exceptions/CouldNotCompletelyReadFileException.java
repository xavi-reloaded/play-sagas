package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class CouldNotCompletelyReadFileException extends BaseAppException {


    private static final String COULD_NOT_COMPLETELY_READ_FILE = "Could not completely read file";

    public CouldNotCompletelyReadFileException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.COULD_NOT_COMPLETELY_READ_FILE, CouldNotCompletelyReadFileException.buildMessage());
    }

    private static String buildMessage() {
        return COULD_NOT_COMPLETELY_READ_FILE;
    }
}
