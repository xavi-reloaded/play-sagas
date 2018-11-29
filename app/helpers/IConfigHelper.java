package helpers;

import java.util.List;

public interface IConfigHelper {
    String getConfigString(String key);
    Boolean getConfigBoolean(String key);
    List<String> getStringList(String key);
}
