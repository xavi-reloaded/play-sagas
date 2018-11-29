package tasks;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class TasksCustomExecutionContext extends CustomExecutionContext {

    @Inject
    public TasksCustomExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "tasks-dispatcher");
    }
}
