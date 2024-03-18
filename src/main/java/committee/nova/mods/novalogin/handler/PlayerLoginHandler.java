package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import committee.nova.mods.novalogin.NovaLogin;
import committee.nova.mods.novalogin.models.LoginPlayer;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PlayerLoginHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/18 2:21
 */
public class PlayerLoginHandler {
    public static PlayerLoginHandler INSTANCE = new PlayerLoginHandler();
    private final Set<LoginPlayer> loginList = ConcurrentHashMap.newKeySet();

    @Nullable
    private LoginPlayer getLoginPlayer(UUID uuid) {
        return loginList.stream().filter(player -> player.uuid.equals(uuid)).findAny().orElse(null);
    }

    public void login(UUID uuid, String pwd){
        LoginPlayer loginPlayer = getLoginPlayer(uuid);

        ServerPlayer player = Const.SERVER.getPlayerList().getPlayer(uuid);
        if (loginPlayer == null || player == null) {
            return;
        }
        if (!NovaLogin.SAVE.isReg(uuid)){
            NovaLogin.SAVE.reg(uuid, pwd);
            Const.LOGGER.info("Player " + loginPlayer.name + "registered!");
        } else if (NovaLogin.SAVE.checkPwd(uuid, pwd)) {
            Const.LOGGER.info("Player " + loginPlayer.name + "logged in!");
        } else {
            Const.LOGGER.warn("Player " + loginPlayer.name + "try to login with wrong password!");
        }
    }

    public void onPlayerJoin(ServerPlayer player){
        LoginPlayer loginPlayer = new LoginPlayer(player);
        loginList.add(loginPlayer);
    }

    public void onPlayerLeave(ServerPlayer player){
        loginList.removeIf(loginPlayer -> loginPlayer.uuid.equals(player.getUUID()));
    }

    public boolean isLogin(UUID uuid){
        return loginList.stream().noneMatch(loginPlayer -> loginPlayer.uuid.equals(uuid));
    }

}
