package helpers.validator.exceptions;

public class IllegalAccessValidation extends BaseValidation {

    private static final String ILLEGAL_ACCESS = "Illegal access";

    public IllegalAccessValidation(String field){
        super(ErrorValidationCodes.ILLEGAL_ACCESS, field , IllegalAccessValidation.buildMessage());
    }

    public static String buildMessage(){
        return ILLEGAL_ACCESS;
    }
}
