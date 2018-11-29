package tasks;

import akka.actor.ActorSystem;
import helpers.CleanTmpFilesHelper;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class CleanTmpFilesTask {

    private final ActorSystem actorSystem;
    private final TasksCustomExecutionContext executor;
    private final CleanTmpFilesHelper cleanTmpFilesHelper;

    @Inject
    public CleanTmpFilesTask(ActorSystem actorSystem, TasksCustomExecutionContext executor, CleanTmpFilesHelper cleanTmpFilesHelper){
        this.actorSystem = actorSystem;
        this.executor = executor;
        this.cleanTmpFilesHelper = cleanTmpFilesHelper;
        this.createScheduler();
    }

    private void createScheduler(){
        this.actorSystem.scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES),
                Duration.create(24, TimeUnit.HOURS),
                this.cleanTmpFilesHelper::cleanTmpFiles,
                this.executor
        );
    }
}
