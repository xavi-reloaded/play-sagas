package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.TranslatableString;
import play.libs.Json;

import java.io.IOException;

public class TranslatableParser {

    private TranslatableParser() {
        throw new IllegalStateException("TranslatableParser class");
    }

    public static TranslatableString get(String field){
        if(field==null){
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(field, TranslatableString.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static String set(TranslatableString field){
        return Json.toJson(field).toString();
    }
}
