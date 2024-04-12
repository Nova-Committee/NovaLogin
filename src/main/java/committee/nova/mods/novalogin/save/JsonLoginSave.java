package committee.nova.mods.novalogin.save;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.models.User;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLPaths;
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
import static committee.nova.mods.novalogin.Const.playerCacheMap;

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

    public JsonLoginSave(){
        this.path = FMLPaths.GAMEDIR.get().resolve("nova").resolve("login");
        try {
            load();
        }catch (IOException e){
            Const.LOGGER.error("加载出错，请检查文件", e);
        }
    }

    @Override
    public boolean checkPwd(String name, String password) {
        if (playerCacheMap.get(name).isRegister && !mojangAccountNamesCache.contains(name)) {
            return BCrypt.checkpw(password, playerCacheMap.get(name).pwd);
        }
        return false;
    }

    @Override
    public void unReg(String name) {
        dirty = true;
        playerCacheMap.remove(name);
    }

    @Override
    public boolean isReg(String name) {
        return playerCacheMap.get(name).isRegister && !mojangAccountNamesCache.contains(name);
    }

    @Override
    public void reg(ServerPlayer player, String password) {
        String name = player.getGameProfile().getName();
        User user = playerCacheMap.get(name);
        if (!user.isRegister && !mojangAccountNamesCache.contains(name)) {
            user.setPwd(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setAuth(false);
            user.setRegister(true);
            playerCacheMap.put(name, user);
            dirty = true;
        }
    }

    @Override
    public void changePwd(ServerPlayer player, String newPassword) {
        String name = player.getGameProfile().getName();
        if (playerCacheMap.get(name).isRegister && !mojangAccountNamesCache.contains(name)) {
            dirty = true;
            User user = playerCacheMap.get(name);
            user.setPwd(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            playerCacheMap.put(name, user);
        }
    }

    @Override
    public boolean dirty() {
        return dirty;
    }

    @Override
    public void save() throws IOException {
        playerCacheMap.forEach(((name, user) -> {
            try {
                if (!Files.exists(this.path.resolve(name + ".json"))) {
                    Files.createDirectories(path);
                    Files.createFile(this.path.resolve(name + ".json"));
                }
                Files.writeString(this.path.resolve(name + ".json"), Const.GSON.toJson(user), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                Const.LOGGER.error("保存出错，请检查文件", e);
            }
        }));
    }

    @Override
    public void load() throws IOException {
        if (!Files.exists(this.path)) Files.createDirectories(path);
        File[] files = path.toFile().listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;
        for (File f : files){
            User user = Const.GSON.fromJson(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8), User.class);
            playerCacheMap.put(user.name, user);
        }
    }

}
