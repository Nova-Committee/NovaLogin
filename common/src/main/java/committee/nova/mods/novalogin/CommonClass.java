package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.save.JsonLoginSave;

import java.io.IOException;
import java.nio.file.Path;

public class CommonClass {
    public static JsonLoginSave SAVE;
    public static void init(Path gamePath) {
        CommonClass.SAVE = new JsonLoginSave(gamePath);
        try {
            CommonClass.SAVE.load();
        } catch (IOException e) {
            Const.LOGGER.error("Failed to load json login save file", e);
        }
    }
}