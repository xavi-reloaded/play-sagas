package helpers;

import org.junit.Assert;
import org.junit.Test;

public class CryptUtilsTest {


    @Test
    public void getStringHash()
    {
        String actual = CryptUtils.getStringHash("pass");
        String expected = "$s0$81010$";
        Assert.assertTrue(actual.startsWith(expected));
    }

    @Test
    public void check() {

    }
}

