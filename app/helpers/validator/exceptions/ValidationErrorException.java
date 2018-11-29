package helpers.validator.exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

import java.util.List;

public class ValidationErrorException extends BaseValidationAppException {

    private static final String VALIDATION_ERROR = "Validation error";

    public ValidationErrorException(List<IBaseValidationException> errors) {
        super(HTTPStatusCodes.UNPROCESSABLE_ENTITY, ErrorCodes.VALIDATION_ERROR, ValidationErrorException.buildMessage(), errors);
    }

    public static String buildMessage() {
        return VALIDATION_ERROR;
    }
}
