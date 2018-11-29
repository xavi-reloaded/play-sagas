package exceptions;

import exceptions.codes.ErrorCodes;
import exceptions.codes.HTTPStatusCodes;

public class UnsupportedDeviceException extends BaseAppException {

    private static final String UNSUPPORTED_DEVICE = "Unsupported device.";

    public UnsupportedDeviceException() {
        super(HTTPStatusCodes.CONFLICT, ErrorCodes.UNSUPPORTED_DEVICE, UnsupportedDeviceException.buildMessage());
    }

    public static String buildMessage() {
        return UNSUPPORTED_DEVICE;
    }
}
