package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class UsernameTakenException extends BaseAppException {

    private static final String USERNAME_TAKEN = "Username taken";

    public UsernameTakenException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.USERNAME_TAKEN, UsernameTakenException.buildMessage());
    }

    public static String buildMessage() {
        return USERNAME_TAKEN;
    }
}
