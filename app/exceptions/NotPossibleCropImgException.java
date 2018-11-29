package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class NotPossibleCropImgException extends BaseAppException {

    private static final String IT_ISN_T_POSSIBLE_TO_CUT_THE_IMAGE = "It isn't possible to crop the image";

    public NotPossibleCropImgException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.IT_ISN_T_POSSIBLE_TO_CUT_THE_IMAGE, NotPossibleCropImgException.buildMessage());
    }

    private static String buildMessage() {
        return IT_ISN_T_POSSIBLE_TO_CUT_THE_IMAGE;
    }
}
