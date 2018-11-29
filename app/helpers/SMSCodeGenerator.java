package helpers;

import java.util.Random;

public class SMSCodeGenerator {

    public int generateCode() {
        return new Random().nextInt(999999 - 100000) + 100000;
    }

}
