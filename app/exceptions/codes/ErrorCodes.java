package exceptions.codes;


public class ErrorCodes {

    private ErrorCodes() {
        throw new IllegalStateException("ErrorCodes class");
    }


    public static final Integer APP_EXCEPTION                                               = 1601;
    public static final Integer VALIDATION_ERROR                                            = 1620;
    public static final Integer WRONG_LOGIN                                                 = 1640;

    public static final Integer ENTITY_NOT_FOUND 			                                = 1502;



    public static final Integer WRONG_FACEBOOK_LOGIN                                        = 1841;
    public static final Integer USERNAME_TAKEN                                              = 1004;

    public static final Integer UNSUPPORTED_DEVICE                                          = 1601;


    public static final Integer NOT_POSSIBLE_VALIDATE_THE_TOKEN                             = 1650;


    public static final Integer IT_ISN_T_POSSIBLE_TO_CUT_THE_IMAGE 	                        = 1701;
    public static final Integer NOT_POSSIBLE_SAVE_FILE 	                                    = 1702;
    public static final Integer NOT_POSSIBLE_CREATE_DIR 	                                = 1703;
    public static final Integer COULD_NOT_COMPLETELY_READ_FILE 			                    = 1704;
    public static final Integer COULD_NOT_COMPLETELY_READ_FILE_AS_IT_IS_TOO_LONG            = 1705;
    public static final Integer FILE_NOT_EXIST                                              = 1706;
    public static final Integer FIELDS_NOT_EXIST 	                                        = 1707;

    public static final Integer SMS_TOO_LONG                                                = 1801;
    public static final Integer PHONE_NOT_REGISTERED                                        = 1802;
    public static final Integer INVALID_PHONE_NUMBER                                        = 1803;
    public static final Integer CODE_NOT_VALID                                              = 1804;
}
