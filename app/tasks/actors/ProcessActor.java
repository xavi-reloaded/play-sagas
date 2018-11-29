package tasks.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import tasks.actors.HelloActorProtocol.SayHello;

public class ProcessActor extends AbstractActor {

    public static Props getProps() {
        return Props.create(ProcessActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JsonNode.class, feedJson -> {
                    System.out.println(feedJson);
                    sender().tell(feedJson.get("id").toString(), self());
                })
                .build();
    }

}