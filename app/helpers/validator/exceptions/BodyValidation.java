package helpers.validator.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BodyValidation implements IBodyValidationException {
    Integer errorCode;
    String  message;
    List<IBaseValidationException> errors;

    public BodyValidation(Integer errorCode, String message, List<IBaseValidationException> errors) {
        this.setErrorCode(errorCode);
        this.setMessage(message);
        this.setErrors(errors);
    }
}
