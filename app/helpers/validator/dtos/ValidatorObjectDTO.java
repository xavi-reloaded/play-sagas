package helpers.validator.dtos;

import helpers.validator.enumerations.ValidatorsEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatorObjectDTO {
    ValidatorsEnum validator;
    String stringValue;
    Integer integerValue;

    public ValidatorObjectDTO(ValidatorsEnum validator) {
        this.setValidator(validator);
    }

    public ValidatorObjectDTO(ValidatorsEnum validator, String value) {
        this(validator);
        setStringValue(value);
    }

    public ValidatorObjectDTO(ValidatorsEnum validator, Integer value) {
        this(validator);
        this.setIntegerValue(value);
    }
}
