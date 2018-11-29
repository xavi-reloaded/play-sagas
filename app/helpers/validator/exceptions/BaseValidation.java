package helpers.validator.exceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseValidation implements IBaseValidationException {

    Integer code ;
    String field;
    String message;
    Object[] extra;

    BaseValidation(Integer code, String field, String message){
        this.setCode(code);
        this.setField(field);
        this.setMessage(message);
    }

    BaseValidation(Integer code, String field, String message, Object[] extra){
        this.setCode(code);
        this.setField(field);
        this.setMessage(message);
        this.setExtra(extra);
    }
}

