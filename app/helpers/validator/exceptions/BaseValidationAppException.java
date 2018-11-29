package helpers.validator.exceptions;

import exceptions.codes.HTTPStatusCodes;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class BaseValidationAppException extends Exception implements IBaseValidationAppException, Serializable {

    final Integer code;
    final transient IBodyValidationException body;

    public BaseValidationAppException(Integer code, Integer apiError, String message, List<IBaseValidationException> errors) {
        super(message);
        this.code = (code != null) ? code : HTTPStatusCodes.UNPROCESSABLE_ENTITY;
        this.body = new BodyValidation(apiError, message, errors);
    }
}
