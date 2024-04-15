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
        String name = player.getGameProfile().getName();
        if (containsKey(name)) {
            return super.get(name);
        }
        LoginUser newPlayer = new LoginUser(player);
        put(name, newPlayer);
        return newPlayer;
    }

    public static class LoginUser{
        public final ServerPlayer player;
        @Setter
        public boolean login = false;
        @Setter
        public boolean reLogin = false;
        public LoginUser(ServerPlayer player) {
            this.player = player;
        }

    }
}
