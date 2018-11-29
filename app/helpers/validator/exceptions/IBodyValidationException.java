package helpers.validator.exceptions;

import java.util.List;

/**
 * Created by eko327 on 15/6/17.
 */
public interface IBodyValidationException {
    void setErrorCode(Integer errorCode);

    void setMessage(String message);

    void setErrors(List<IBaseValidationException> errors);

    Integer getErrorCode();

    String getMessage();

    List<IBaseValidationException> getErrors();
}
