package controllers;


import akka.actor.*;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import tasks.actors.HelloActor;
import tasks.actors.HelloActorProtocol;

import javax.inject.*;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

@Singleton
public class SampleLaunchActorController extends Controller {

    final ActorRef helloActor;

    @Inject
    public SampleLaunchActorController(ActorSystem system) {
        helloActor = system.actorOf(HelloActor.getProps());
    }

    public CompletionStage<Result> sayHello(String name) {
        return FutureConverters.toJava(ask(helloActor, new HelloActorProtocol.SayHello(name), 1000))
                .thenApply(response -> ok((String) response));
    }
}