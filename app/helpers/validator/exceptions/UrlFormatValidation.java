package helpers.validator.exceptions;

public class UrlFormatValidation extends BaseValidation {

    private static final String WRONG_URL_FORMAT = "Wrong url format";

    public UrlFormatValidation(String field){
        super(ErrorValidationCodes.WRONG_URL_FORMAT, field , UrlFormatValidation.buildMessage());
    }

    public static String buildMessage(){
        return WRONG_URL_FORMAT;
    }
}
