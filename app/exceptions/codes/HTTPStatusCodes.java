package exceptions.codes;


public class HTTPStatusCodes {

    private HTTPStatusCodes() {
        throw new IllegalStateException("HTTPStatusCodes class");
    }

    public static final Integer CONTINUE                            = 100;
    public static final Integer SWITCHING_PROTOCOLS                 = 101;
    public static final Integer OK                                  = 200;
    public static final Integer CREATED                             = 201;
    public static final Integer ACCEPTED                            = 202;
    public static final Integer NON_AUTHORITATIVE_INFORMATION       = 203;
    public static final Integer NO_CONTENT                          = 204;
    public static final Integer RESET_CONTENT                       = 205;
    public static final Integer PARTIAL_CONTENT                     = 206;
    public static final Integer MULTIPLE_CHOICES                    = 300;
    public static final Integer MOVED_PERMANENTLY                   = 301;
    public static final Integer FOUND                               = 302;
    public static final Integer SEE_OTHER                           = 303;
    public static final Integer NOT_MODIFIED                        = 304;
    public static final Integer USE_PROXY                           = 305;
    public static final Integer TEMPORARY_REDIRECT                  = 307;
    public static final Integer BAD_REQUEST                         = 400;
    public static final Integer UNAUTHORIZED                        = 401;
    public static final Integer PAYMENT_REQUIRED                    = 402;
    public static final Integer FORBIDDEN                           = 403;
    public static final Integer NOT_FOUND                           = 404;
    public static final Integer METHOD_NOT_ALLOWED                  = 405;
    public static final Integer NOT_ACCEPTABLE                      = 406;
    public static final Integer PROXY_AUTHENTICATION_REQUIRED       = 407;
    public static final Integer REQUEST_TIMEOUT                     = 408;
    public static final Integer CONFLICT                            = 409;
    public static final Integer GONE                                = 410;
    public static final Integer LENGTH_REQUIRED                     = 411;
    public static final Integer PRECONDITION_FAILED                 = 412;
    public static final Integer REQUEST_ENTITY_TOO_LARGE            = 413;
    public static final Integer REQUEST_URI_TOO_LONG                = 414;
    public static final Integer UNSUPPORTED_MEDIA_TYPE              = 415;
    public static final Integer REQUESTED_RANGE_NOT_SATISFIABLE     = 416;
    public static final Integer EXPECTATION_FAILED                  = 417;
    public static final Integer UNPROCESSABLE_ENTITY                = 422;
    public static final Integer TOO_MANY_REQUESTS                   = 429;
    public static final Integer INTERNAL_SERVER_ERROR               = 500;
    public static final Integer NOT_IMPLEMENTED                     = 501;
    public static final Integer BAD_GATEWAY                         = 502;
    public static final Integer SERVICE_UNAVAILABLE                 = 503;
    public static final Integer GATEWAY_TIMEOUT                     = 504;
    public static final Integer HTTP_VERSION_NOT_SUPPORTED          = 505;
}
