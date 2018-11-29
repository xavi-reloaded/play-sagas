package adapters;

import play.inject.Injector;
import queues.IQueueParser;
import redis.clients.jedis.Jedis;

public class RedisAdapter implements IBaseQueue {

    private String topic = "queue:actorSampleJob";
    private final Jedis q;

    public RedisAdapter() {
        q = new Jedis("localhost");
    }

    public RedisAdapter(String queue) {
        q = new Jedis("localhost");
        topic = queue;
    }



    @Override
    public String put(String msg) {
        return String.valueOf(q.lpush(topic, msg));
    }

    @Override
    public String get() {
        return q.rpop(topic);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public IQueueParser getParser(Injector injector) {
        return null;
    }
}
