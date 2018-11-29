package helpers.validator.exceptions;

import java.text.MessageFormat;

public class MinLengthValidation extends BaseValidation {

    private static final String MINIMUM_CHARACTERS = "Minimum {0} characters";

    public MinLengthValidation(String field, Integer minCharacters) {
        super(ErrorValidationCodes.WRONG_MIN_LENGTH, field, MinLengthValidation.buildMessage(minCharacters), new Object[]{minCharacters});
    }

    public static String buildMessage(Integer minCharacters) {
        return MessageFormat.format(MINIMUM_CHARACTERS, minCharacters);
    }
}
