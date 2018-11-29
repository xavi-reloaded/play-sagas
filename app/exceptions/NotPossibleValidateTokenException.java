package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class NotPossibleValidateTokenException extends BaseAppException {

    private static final String NOT_POSSIBLE_VALIDATE_THE_TOKEN = "Not possible validate the token";

    public NotPossibleValidateTokenException(){
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.NOT_POSSIBLE_VALIDATE_THE_TOKEN, NotPossibleValidateTokenException.buildMessage());
    }

    public static String buildMessage(){
        return NOT_POSSIBLE_VALIDATE_THE_TOKEN;
    }
}
