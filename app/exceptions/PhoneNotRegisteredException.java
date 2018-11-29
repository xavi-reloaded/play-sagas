package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class PhoneNotRegisteredException extends BaseAppException {

    private static final String THE_USER_S_PHONE_IS_NOT_REGISTERED = "The user's phone is not registered";

    public PhoneNotRegisteredException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.PHONE_NOT_REGISTERED, PhoneNotRegisteredException.buildMessage());
    }

    public static String buildMessage() {
        return THE_USER_S_PHONE_IS_NOT_REGISTERED;
    }
}
