package adapters;

import play.inject.Injector;
import queues.IQueueParser;

/**
 * Created by xavi on 03/03/2017.
 */
public interface IBaseQueue {

    public String put(String msg);
    public String get();
    public String getTopic();
    public IQueueParser getParser(Injector injector);

}
