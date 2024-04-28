package committee.nova.mods.novalogin.utils;

import committee.nova.mods.novalogin.Const;
import net.minecraft.ChatFormatting;

/**
 * Log
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/20 下午4:02
 */
public class Log {
    public static void info(String message) {
        Const.LOGGER.info("{}[NOVA-INFO] {}{}", ChatFormatting.BLUE, ChatFormatting.GREEN,message);
    }

    public static void debug(String message) {
        Const.LOGGER.info("{}[NOVA-DEBUG] {}{}", ChatFormatting.AQUA, ChatFormatting.GREEN, message);
    }

    public static void error(String message) {
        Const.LOGGER.error("{}[NOVA-ERROR] {}{}", ChatFormatting.RED, ChatFormatting.GREEN, message);
    }

}
