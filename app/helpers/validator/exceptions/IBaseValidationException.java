package helpers.validator.exceptions;

/**
 * Created by eko327 on 15/6/17.
 */
public interface IBaseValidationException {
    void setCode(Integer code);

    void setField(String field);

    void setMessage(String message);

    Integer getCode();

    String getField();

    String getMessage();
}
