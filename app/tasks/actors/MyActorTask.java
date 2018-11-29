package tasks.actors;

import javax.inject.Named;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.Logger;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class MyActorTask {

    private final ActorRef someActor;
    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public MyActorTask(@Named("hello-actor") ActorRef someActor, ActorSystem actorSystem,
                       ExecutionContext executionContext) {
        this.someActor = someActor;
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        Logger.info("init MyActorTask ");
        this.initialize();
    }

    private void initialize() {
        actorSystem.scheduler().schedule(
                Duration.create(0, TimeUnit.SECONDS), // initialDelay
                Duration.create(30, TimeUnit.SECONDS), // interval
                someActor,
                new HelloActorProtocol.SayHello("coolName"), // message,
                executionContext,
                ActorRef.noSender()
        );

    }
}