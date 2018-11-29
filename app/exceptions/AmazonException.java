package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class AmazonException extends BaseAppException {
    public AmazonException(String message){
        super(HTTPStatusCodes.BAD_REQUEST, ErrorCodes.WRONG_FACEBOOK_LOGIN, AmazonException.buildMessage(message));
    }

    public static String buildMessage(String message){
        return message;
    }
}
