package helpers.validator;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.validator.dtos.ValidatorObjectDTO;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class ValidatorParams {
    final JsonNode object;
    final LinkedHashMap<String, List<ValidatorObjectDTO>> validators;

    public ValidatorParams(JsonNode object, LinkedHashMap<String, List<ValidatorObjectDTO>> validators) {
        this.object = object;
        this.validators = validators;
    }
}
