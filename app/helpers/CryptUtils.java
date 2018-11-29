package helpers;

import com.lambdaworks.crypto.SCryptUtil;

public class CryptUtils {

    private CryptUtils() {
        throw new IllegalStateException("CryptUtils class");
    }

    public static String getStringHash(String toHash) {
        return SCryptUtil.scrypt(toHash, 256, 16, 16);
    }
    public static Boolean check (String toCheck, String encripted){ return  SCryptUtil.check(toCheck, encripted); }
}
