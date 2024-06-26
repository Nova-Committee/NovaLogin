package committee.nova.mods.novalogin.models;

import lombok.Setter;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;

/**
 * LoginUser
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 上午11:21
 */
public class LoginUsers extends HashMap<String, LoginUsers.LoginUser> {

    public static LoginUsers INSTANCE = new LoginUsers();

    public LoginUser get(ServerPlayer player) {
        String username = player.getGameProfile().getName();
        if (containsKey(username)) {
            return super.get(username);
        }
        LoginUser newPlayer = new LoginUser();
        put(username, newPlayer);
        return newPlayer;
    }

    @Setter
    public static class LoginUser{
        public boolean login = false;
        public boolean reLogin = false;
    }
}
