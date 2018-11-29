package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class CouldNotCompletelyReadFileItIsTooLongException extends BaseAppException {


    private static final String COULD_NOT_COMPLETELY_READ_FILE_AS_IT_IS_TOO_LONG = "Could not completely read file as it is too long";

    public CouldNotCompletelyReadFileItIsTooLongException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.COULD_NOT_COMPLETELY_READ_FILE_AS_IT_IS_TOO_LONG, CouldNotCompletelyReadFileItIsTooLongException.buildMessage());
    }

    private static String buildMessage() {
        return COULD_NOT_COMPLETELY_READ_FILE_AS_IT_IS_TOO_LONG;
    }
}
