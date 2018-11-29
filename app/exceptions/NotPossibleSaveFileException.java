package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class NotPossibleSaveFileException extends BaseAppException {

    private static final String IT_ISN_T_POSSIBLE_TO_SAVE_THE_FILE = "It isn't possible to save the file.";

    public NotPossibleSaveFileException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.NOT_POSSIBLE_SAVE_FILE, NotPossibleSaveFileException.buildMessage());
    }

    private static String buildMessage() {
        return IT_ISN_T_POSSIBLE_TO_SAVE_THE_FILE;
    }
}
