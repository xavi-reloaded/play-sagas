package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class NotPossibleCreateDirException extends BaseAppException {

    private static final String IT_ISN_T_POSSIBLE_TO_CREATE_THE_DIR = "It isn't possible to create the dir.";

    public NotPossibleCreateDirException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.NOT_POSSIBLE_CREATE_DIR, NotPossibleCreateDirException.buildMessage());
    }

    private static String buildMessage() {
        return IT_ISN_T_POSSIBLE_TO_CREATE_THE_DIR;
    }
}
