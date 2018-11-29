package modules;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import play.Logger;

/**
 * Created by ecobos on 6/9/17.
 */
@Singleton
public class OnStartUpClass {
    @Inject
    public OnStartUpClass(Config config) {
        Logger.info("FeedService-processor has started in mode: " + config.getString("app.environment"));
    }
}
