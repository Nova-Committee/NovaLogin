package committee.nova.mods.novalogin;

import committee.nova.mods.novalogin.config.ConfigHandler;
import committee.nova.mods.novalogin.save.JsonLoginSave;
import committee.nova.mods.novalogin.save.LocalUserSave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static committee.nova.mods.novalogin.Const.novaPath;

public class CommonClass {


    public static void init(Path gamePath) {
        try {
            novaPath = gamePath.resolve("nova").resolve("login");
            if (!Files.exists(novaPath)) Files.createDirectories(novaPath);
            Const.loginSave = new JsonLoginSave(novaPath);
            Const.configHandler = new ConfigHandler(novaPath);
        } catch (IOException e) {
            Const.LOGGER.error("Failed to load file", e);
        }
    }

    public static void clientInit() {
        LocalUserSave.load();
    }

}