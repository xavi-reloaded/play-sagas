package helpers.validator.exceptions;

import java.text.MessageFormat;

public class MaxLengthValidation extends BaseValidation {

    private static final String MAXIMUM_CHARACTERS = "Maximum {0} characters";

    public MaxLengthValidation(String field, Integer maxCharacters){
        super(ErrorValidationCodes.WRONG_MAX_LENGTH, field , MaxLengthValidation.buildMessage(maxCharacters), new Object[]{maxCharacters});
    }

    public static String buildMessage(Integer maxCharacters){
        return MessageFormat.format(MAXIMUM_CHARACTERS, maxCharacters);
    }
}
