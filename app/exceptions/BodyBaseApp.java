package exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BodyBaseApp implements IBodyBaseApp {
    Integer errorCode;
    String message;

    public BodyBaseApp(Integer errorCode, String message) {
        this.setErrorCode(errorCode);
        this.setMessage(message);
    }
}
