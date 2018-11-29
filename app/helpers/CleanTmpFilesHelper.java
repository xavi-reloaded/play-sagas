package helpers;

import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

@Singleton
public class CleanTmpFilesHelper {

    private String tmpDataFolder;

    @Inject
    public CleanTmpFilesHelper(ConfigHelper configHelper) {
        this.tmpDataFolder = configHelper.getConfigString(ConfigHelper.APP_TMP_FOLDER);
    }

    public void cleanTmpFiles() {

        File dir = new File(this.tmpDataFolder);

        File[] files = dir.listFiles();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -24);
        Date limitDate = cal.getTime();

        if (files != null) {
            for (File file : files) {
                Date modifiedDate = new Date(file.lastModified());
                if (modifiedDate.before(limitDate)) {
                    try {
                        Files.delete(Paths.get(file.getPath()));
                    } catch (IOException e) {
                        Logger.error(this.getClass().getSimpleName(), e.getMessage());
                    }
                }
            }
        }
    }
}