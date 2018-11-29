package factories;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import helpers.MessagesHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;

@RunWith(DataProviderRunner.class)
public class NotificationFactoryTests {

    private NotificationFactory sut;

    @Before
    public void setUp(){
        MessagesHelper messagesHelper = mock(MessagesHelper.class);
        this.sut = new NotificationFactory(messagesHelper);
    }

    @Test
    public void test_empty(){

    }


}
