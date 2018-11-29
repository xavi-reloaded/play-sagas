package exceptions;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BaseAppException extends Exception implements Serializable {

    final Integer code;
    final transient IBodyBaseApp body;

    public BaseAppException(Integer code, Integer apiError, String message) {
        super(message);
        this.code = code;
        this.body = new BodyBaseApp(apiError, message);
    }
}
