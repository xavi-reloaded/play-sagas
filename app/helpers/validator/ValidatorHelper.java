package helpers.validator;

import com.fasterxml.jackson.databind.JsonNode;
import helpers.validator.dtos.ValidatorObjectDTO;
import helpers.validator.enumerations.ValidatorsEnum;
import helpers.validator.exceptions.*;
import play.libs.Json;

import javax.inject.Singleton;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;


@Singleton
public class ValidatorHelper {

    public Boolean validate(ValidatorParams validatorParams) throws ValidationErrorException {
        List<IBaseValidationException> errors = new ArrayList<>();

        for (Map.Entry<String, List<ValidatorObjectDTO>> entry : validatorParams.getValidators().entrySet()) {
            String key = entry.getKey();
            List<ValidatorObjectDTO> value = entry.getValue();
            for (ValidatorObjectDTO validator : value) {
                IBaseValidationException exception = null;
                if (validator.getValidator() == ValidatorsEnum.REQUIRED) {
                    exception = this.validateRequired(validatorParams.getObject(), key);
                } else if (validator.getValidator() == ValidatorsEnum.EMAIL) {
                    exception = this.validateEmail(validatorParams.getObject(), key);
                } else if (validator.getValidator() == ValidatorsEnum.URL) {
                    exception = this.validateURL(validatorParams.getObject(), key);
                } else if (validator.getValidator() == ValidatorsEnum.EQUAL_TO) {
                    exception = this.validateEqualTo(validatorParams.getObject(), key, validator.getStringValue());
                } else if (validator.getValidator() == ValidatorsEnum.MIN_LENGTH) {
                    exception = this.validateMinLength(validatorParams.getObject(), key, validator.getIntegerValue());
                } else if (validator.getValidator() == ValidatorsEnum.MAX_LENGTH) {
                    exception = this.validateMaxLength(validatorParams.getObject(), key, validator.getIntegerValue());
                }

                if (exception != null) {
                    errors.add(exception);
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationErrorException(errors);
        }
        return true;
    }

    IBaseValidationException validateRequired(JsonNode object, String key) {
        IBaseValidationException exception = null;
        try {
            JsonNode value = this.getValue(object, key);
            String valueString = Json.fromJson(value, String.class);
            if (valueString == null || Objects.equals(valueString, "")) {
                exception = new RequiredValidation(key);
            }
        } catch (NullPointerException e) {
            exception = new RequiredValidation(key);
        }
        return exception;
    }

    IBaseValidationException validateEmail(JsonNode object, String key) {
        IBaseValidationException exception = null;
        try {
            JsonNode value = this.getValue(object, key);
            String email = Json.fromJson(value, String.class);
            if (!Objects.equals(email, "")) {
                Pattern ptr = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                if (!ptr.matcher(email).matches()) {
                    exception = new EmailFormatValidation(key);
                }
            } else {
                exception = new EmailFormatValidation(key);
            }
        } catch (NullPointerException e) {
            exception = new IllegalAccessValidation(key);
        }

        return exception;
    }

    IBaseValidationException validateURL(JsonNode object, String key) {
        IBaseValidationException exception = null;
        try {
            JsonNode value = this.getValue(object, key);
            String url = Json.fromJson(value, String.class);
            URI uri = new URI(url);
            if (uri.getHost() == null) {
                exception = new UrlFormatValidation(key);
            }
        } catch (URISyntaxException e) {
            exception = new UrlFormatValidation(key);
        } catch (NullPointerException e) {
            exception = new IllegalAccessValidation(key);
        }
        return exception;
    }

    IBaseValidationException validateEqualTo(JsonNode object, String key, String key2) {
        IBaseValidationException exception = null;
        JsonNode value1 = null;
        JsonNode value2 = null;
        try {
            value1 = this.getValue(object, key);
        } catch (NullPointerException e) {
            exception = new IllegalAccessValidation(key);
        }
        if (exception == null) {
            try {
                value2 = this.getValue(object, key2);
            } catch (NullPointerException e) {
                exception = new IllegalAccessValidation(key2);
            }
        }
        if (exception == null) {
            String value1String = Json.fromJson(value1, String.class);
            String value2String = Json.fromJson(value2, String.class);
            if (!value1String.equals(value2String)) {
                exception = new EqualToValidation(key, key2);
            }
        }
        return exception;
    }

    IBaseValidationException validateMinLength(JsonNode object, String key, Integer minLength) {
        IBaseValidationException exception = null;
        try {
            JsonNode value = this.getValue(object, key);
            String valueString = Json.fromJson(value, String.class);
            if (!valueString.isEmpty() && (valueString.length() < minLength)) {
                exception = new MinLengthValidation(key, minLength);
            }
        } catch (NullPointerException e) {
            exception = new IllegalAccessValidation(key);
        }
        return exception;
    }

    IBaseValidationException validateMaxLength(JsonNode object, String key, Integer maxLength) {
        IBaseValidationException exception = null;
        try {
            JsonNode value = this.getValue(object, key);
            String valueString = Json.fromJson(value, String.class);
            if (valueString.length() > maxLength) {
                exception = new MaxLengthValidation(key, maxLength);
            }
        } catch (NullPointerException e) {
            exception = new IllegalAccessValidation(key);
        }

        return exception;
    }

    JsonNode getValue(JsonNode object, String key) {
        JsonNode value = object.get(key);
        if (value == null) {
            throw new NullPointerException();
        }
        return value;
    }
}