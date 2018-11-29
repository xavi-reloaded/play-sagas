package factories;

import com.google.inject.Inject;
import helpers.MessagesHelper;

public class NotificationFactory {

    private final MessagesHelper messagesHelper;

    @Inject
    public NotificationFactory(MessagesHelper messagesHelper){
        this.messagesHelper = messagesHelper;
    }
}