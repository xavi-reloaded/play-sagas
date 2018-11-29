package modules;

import com.google.inject.AbstractModule;
import tasks.actors.MyActorTask;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `OnStartUpModule` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class OnStartUpModule extends AbstractModule {

    @Override
    public void configure() {
        bind(OnStartUpClass.class).asEagerSingleton();
    }

}
