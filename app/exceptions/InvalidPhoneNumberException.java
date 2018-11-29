package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class InvalidPhoneNumberException extends BaseAppException {

    private static final String INVALID_PHONE_NUMBER = "Invalid phone number";

    public InvalidPhoneNumberException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.INVALID_PHONE_NUMBER, InvalidPhoneNumberException.buildMessage());
    }

    public static String buildMessage() {
        return INVALID_PHONE_NUMBER;
    }
}
