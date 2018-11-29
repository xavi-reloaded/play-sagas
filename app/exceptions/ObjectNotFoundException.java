package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

/**
 * Created by rmatei9 on 17/2/17.
 */
public class ObjectNotFoundException extends BaseAppException {

    private static final String OBJECT_DOES_NOT_EXIST = "Object does not exist";

    public ObjectNotFoundException() {
        super(HTTPStatusCodes.NOT_FOUND, ErrorCodes.ENTITY_NOT_FOUND, ObjectNotFoundException.buildMessage());
    }

    public static String buildMessage() {
        return OBJECT_DOES_NOT_EXIST;
    }
}
