package tasks;

import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
import tasks.actors.HelloActor;

public class AvailableActorsModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        bindActor(HelloActor.class, "hello-actor");
    }
}