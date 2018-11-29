package helpers.validator.exceptions;

public class EmailFormatValidation extends BaseValidation {

    private static final String WRONG_EMAIL_FORMAT = "Wrong email format";

    public EmailFormatValidation(String field){
        super(ErrorValidationCodes.WRONG_EMAIL_FORMAT, field , EmailFormatValidation.buildMessage());
    }

    public static String buildMessage(){
        return WRONG_EMAIL_FORMAT;
    }
}
