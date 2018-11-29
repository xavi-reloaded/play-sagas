package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class CodeNotValidException extends BaseAppException {

    private static final String THE_CODE_ENTERED_IS_NOT_VALID = "The code entered is not valid";

    public CodeNotValidException(){
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.CODE_NOT_VALID, CodeNotValidException.buildMessage());
    }
    
    public static String buildMessage(){
        return THE_CODE_ENTERED_IS_NOT_VALID;
    }
}
