package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

/**
 * Created by rmatei9 on 17/2/17.
 */
public class WrongLoginException extends BaseAppException {

    private static final String IS_WRONG = "Email or password is wrong.";

    public WrongLoginException(){
        super(HTTPStatusCodes.UNAUTHORIZED, ErrorCodes.WRONG_LOGIN, WrongLoginException.buildMessage());
    }

    public static String buildMessage(){
        return IS_WRONG;
    }
}
