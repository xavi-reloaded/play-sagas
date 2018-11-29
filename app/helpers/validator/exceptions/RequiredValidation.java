package helpers.validator.exceptions;

public class RequiredValidation extends BaseValidation {

    private static final String FIELD_IS_MANDATORY = "Field is mandatory";

    public RequiredValidation(String field){
        super(ErrorValidationCodes.REQUIRED, field , RequiredValidation.buildMessage());
    }

    public static String buildMessage(){
        return FIELD_IS_MANDATORY;
    }
}
