package committee.nova.mods.novalogin.handler;

import committee.nova.mods.novalogin.Const;
import net.minecraft.server.level.ServerPlayer;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * OnPlayerReLogin
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/12 下午1:42
 */
public class OnPlayerReLogin {
    public static boolean canReLogin(ServerPlayer player) {
        String name = player.getGameProfile().getName();
        long timestamp = Const.playerCacheMap.get(name).lastLeaveTime;
        if (timestamp != 0){
            LocalDateTime start = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            LocalDateTime end = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            return Duration.between(start, end).toMinutes() < 2;
        }
        return false;
    }
}
