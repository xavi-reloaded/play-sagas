package helpers.validator.exceptions;

import java.text.MessageFormat;

public class EqualToValidation extends BaseValidation {

    private static final String NOT_EQUAL_TO = "{0} not equal to {1}";

    public EqualToValidation(String field, String field2){
        super(ErrorValidationCodes.WRONG_MIN_LENGTH, field , EqualToValidation.buildMessage(field, field2));
    }

    public static String buildMessage(String field1, String fiueld2){
        return MessageFormat.format(NOT_EQUAL_TO, field1, fiueld2);
    }
}
