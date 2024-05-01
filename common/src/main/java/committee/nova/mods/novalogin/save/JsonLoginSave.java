package committee.nova.mods.novalogin.save;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static committee.nova.mods.novalogin.Const.mojangAccountNamesCache;
import static committee.nova.mods.novalogin.Const.playerStorageMap;

/**
 * JsonLoginSave
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:41
 */
public class JsonLoginSave implements LoginSave{
    private final Path path;
    private boolean dirty = false;

    public JsonLoginSave(Path novaPath){
        this.path = novaPath;
        load();
    }

    @Override
    public boolean checkPwd(String name, String password) {
        if (playerStorageMap.get(name).isRegister && !mojangAccountNamesCache.contains(name)) {
            return BCrypt.checkpw(password, playerStorageMap.get(name).pwd);
        }
        return false;
    }

    @Override
    public void unReg(String name) {
        dirty = true;
        playerStorageMap.remove(name);
    }

    @Override
    public boolean isReg(String name) {
        return playerStorageMap.get(name).isRegister && !mojangAccountNamesCache.contains(name);
    }

    @Override
    public void reg(ServerPlayer player, String password) {
        String name = player.getGameProfile().getName();
        User user = playerStorageMap.get(name);
        if (!user.isRegister && !mojangAccountNamesCache.contains(name)) {
            user.setPwd(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setPremium(false);
            user.setRegister(true);
            playerStorageMap.put(name, user);
            dirty = true;
        }
    }

    @Override
    public void changePwd(ServerPlayer player, String newPassword) {
        String name = player.getGameProfile().getName();
        if (playerStorageMap.get(name).isRegister && !mojangAccountNamesCache.contains(name)) {
            dirty = true;
            User user = playerStorageMap.get(name);
            user.setPwd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            playerStorageMap.put(name, user);
        }
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void save(){
        playerStorageMap.forEach(((name, user) -> {
            try {
                if (!Files.exists(this.path.resolve(name + ".json"))) {
                    Files.createDirectories(path);
                    Files.createFile(this.path.resolve(name + ".json"));
                }
                Files.writeString(this.path.resolve(name + ".json"), Const.GSON.toJson(user), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                Const.LOGGER.error("Save Error:", e);
            }
        }));
    }

    @Override
    public void load(){
        try {
        File[] files = path.toFile().listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;
        for (File f : files){
            User user = Const.GSON.fromJson(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8), User.class);
            playerStorageMap.put(user.name, user);
        }
        } catch (IOException e) {
            Const.LOGGER.error("Load Error:", e);
        }
    }

}
