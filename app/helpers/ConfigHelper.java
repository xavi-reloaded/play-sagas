package helpers;

import com.typesafe.config.Config;

import javax.inject.Inject;
import java.util.List;

public class ConfigHelper implements IConfigHelper {

    private Config config;

    public static final String SECRET_KEY = "app.secret";
    public static final String APP_NAME = "app.name";
    public static final String TOKEN_EXPIRY_MINUTES = "app.token_expiry_minutes";

    public static final String APP_FILES_FOLDER = "app.folder.files";
    public static final String APP_TMP_FOLDER = "app.folder.tmp";

    @Inject
    public ConfigHelper(Config config) {
        this.config = config;
    }

    @Override
    public String getConfigString(String key) {
        return this.config.getString(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return this.config.getStringList(key);
    }

    @Override
    public Boolean getConfigBoolean(String key) {
        return this.config.getBoolean(key);
    }

    public String getEnvironment() {
        return this.getConfigString("app.environment");
    }
}
