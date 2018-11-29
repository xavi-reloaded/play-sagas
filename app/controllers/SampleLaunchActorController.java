package controllers;


import akka.actor.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import tasks.actors.HelloActor;
import tasks.actors.HelloActorProtocol;
import tasks.actors.ProcessActor;

import javax.inject.*;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

@Singleton
public class SampleLaunchActorController extends BaseController {

    final ActorRef helloActor;
    final ActorRef processActor;

    @Inject
    public SampleLaunchActorController(ActorSystem system) {
        helloActor = system.actorOf(HelloActor.getProps());
        processActor = system.actorOf(ProcessActor.getProps());
    }

    public CompletionStage<Result> sayHello(String name) {
        return FutureConverters.toJava(ask(helloActor, new HelloActorProtocol.SayHello(name), 1000))
                .thenApply(response -> ok((String) response));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> processFeed() {
        JsonNode body = this.getHttpContext().request().body().asJson();
        return FutureConverters.toJava(ask(processActor, body, 5))
                .thenApply(response -> ok((String) response));
    }


}