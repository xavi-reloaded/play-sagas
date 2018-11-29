package helpers.validator.exceptions;

public class ErrorValidationCodes {

    private ErrorValidationCodes() {
        throw new IllegalStateException("Utility class");
    }

    public static final Integer ILLEGAL_ACCESS          = 100;
    public static final Integer REQUIRED                = 110;
    public static final Integer WRONG_EMAIL_FORMAT      = 101;
    public static final Integer WRONG_URL_FORMAT        = 102;
    public static final Integer WRONG_MIN_LENGTH        = 103;
    public static final Integer WRONG_MAX_LENGTH        = 104;

}
