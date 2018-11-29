package exceptions;

import com.google.common.base.Joiner;
import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

import java.util.List;

public class FieldsNotExistException extends BaseAppException {

    public FieldsNotExistException(List<String> fields){
        super(HTTPStatusCodes.NOT_FOUND, ErrorCodes.FIELDS_NOT_EXIST , FieldsNotExistException.buildMessage(fields));
    }

    public static String buildMessage(List<String> fields){
        return  "The fields ("+Joiner.on(", ").skipNulls().join(fields)+") not exist.";
    }
}
