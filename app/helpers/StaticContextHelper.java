package helpers;

import play.mvc.Http;

public class StaticContextHelper {

    private StaticContextHelper() {
        throw new IllegalStateException("StaticContextHelper class");
    }

    public static final String CURRENT_TOKEN = "X-AUTH-TOKEN";

    public static Object get(String key){
        return Http.Context.current().args.get(key);
    }

    public static void put(String key, Object value){
        Http.Context.current().args.put(key, value);
    }

    public static void setToken(String token){
        StaticContextHelper.setCurrentToken(token);
    }

    public static void setCurrentToken(String token){
        put(CURRENT_TOKEN, token);
    }

    public static String getCurrentToken(){
        return (String) get(CURRENT_TOKEN);
    }

}

