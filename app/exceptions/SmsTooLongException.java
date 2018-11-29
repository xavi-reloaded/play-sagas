package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

/**
 * Created by rmatei9 on 17/2/17.
 */
public class SmsTooLongException extends BaseAppException {

    private static final String SMS_TOO_LONG = "SMS too long";

    public SmsTooLongException(){
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.SMS_TOO_LONG, SmsTooLongException.buildMessage());
    }

    public static String buildMessage(){
        return SMS_TOO_LONG;
    }
}
