package adapters;

import org.junit.Assert;
import org.junit.Test;

public class RedisAdapterTest {


    private final RedisAdapter sut;

    public RedisAdapterTest() {

        sut = new RedisAdapter();
    }

    @Test
    public void put() {
        sut.put("hola");
    }

    @Test
    public void get() {
        sut.put("hola");
        String actual = sut.get();
        Assert.assertEquals("hola",actual);
    }

    @Test
    public void getQueue() {
        String actual = sut.getTopic();
        Assert.assertEquals("queue:actorSampleJob",actual);
    }

}