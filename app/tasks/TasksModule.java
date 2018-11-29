package tasks;

import com.google.inject.AbstractModule;
import tasks.actors.MyActorTask;

public class TasksModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CleanTmpFilesTask.class).asEagerSingleton();
        bind(MyActorTask.class).asEagerSingleton();
    }

}
