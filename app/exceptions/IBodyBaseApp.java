package exceptions;

/**
 * Created by eko327 on 15/6/17.
 */
public interface IBodyBaseApp {
    void setErrorCode(Integer errorCode);

    void setMessage(String message);

    Integer getErrorCode();

    String getMessage();
}
