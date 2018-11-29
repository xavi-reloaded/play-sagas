package helpers;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ecobos on 5/30/17.
 */
@RunWith(DataProviderRunner.class)
public class ReflectionUtilsTests {
    public ReflectionUtils sut;

    @DataProvider
    public static Object[][] isFieldTypeDataProvider() {
        return new Object[][] {
                { User.class, "phone", Long.class },
                { User.class, "username", String.class },
        };
    }

    @Before
    public void setUp(){
        this.sut = new ReflectionUtils();
    }

    @Test
    @UseDataProvider("isFieldTypeDataProvider")
    public void test_compareClass(Class testClass, String fieldToCompareInClass, Class fieldToAssert) throws NoSuchFieldException, ClassNotFoundException {
        Boolean actual = this.sut.isFieldType(testClass, fieldToCompareInClass, fieldToAssert);
        Assert.assertEquals(true, actual);
    }
}
