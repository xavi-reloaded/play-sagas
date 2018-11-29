package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dtos.TranslatableString;
import play.i18n.Lang;
import play.i18n.Langs;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MessagesHelper implements IMessagesHelper {

    private final MessagesApi messagesApi;
    private final Langs langs;

    @Inject
    MessagesHelper(MessagesApi messagesApi, Langs langs) {
        this.messagesApi = messagesApi;
        this.langs = langs;
    }

    public String get(String key, Object... args) {
        Http.Request request = Http.Context.current().request();
        Messages messages = messagesApi.preferred(request);
        return messages.at(key, args);
    }

    public TranslatableString getTranstableString(String key, Object... args){
        List<Lang> availables = langs.availables();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode language = mapper.createObjectNode();
        for(Lang lang : availables){
            language.put(lang.code(), this.getByLanguage(lang, key, args));
        }
        return Json.fromJson(language, TranslatableString.class);
    }

    public String getByLanguage(Lang language, String key, Object... args){
        return messagesApi.get(language, key, args);
    }
}
