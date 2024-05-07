package committee.nova.mods.novalogin.utils;

import committee.nova.mods.novalogin.Const;
import net.minecraft.util.text.TextFormatting;

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
        Const.LOGGER.info("{}[NOVA-INFO] {}{}", TextFormatting.BLUE, TextFormatting.GREEN,message);
    }

    public static void debug(String message) {
        Const.LOGGER.info("{}[NOVA-DEBUG] {}{}", TextFormatting.AQUA, TextFormatting.GREEN, message);
    }

    public static void error(String message) {
        Const.LOGGER.error("{}[NOVA-ERROR] {}{}", TextFormatting.RED, TextFormatting.GREEN, message);
    }

}
