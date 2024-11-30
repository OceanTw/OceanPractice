package lol.oce.marine.utils;

import lol.oce.marine.Practice;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@UtilityClass
public class ConsoleUtils {

    public static void info(String message) {
        log(ChatColor.BLUE + "[INFO] " + message);
    }

    public static void warn(String message) {
        log(ChatColor.YELLOW + "[WARN] " + message);
    }

    public static void error(String message) {
        log(ChatColor.RED + "[ERROR] " + message);
    }

    public static void debug(String message) {
        if (Practice.getSettingsConfig().getConfiguration().getBoolean("debug")) {
            log(ChatColor.GRAY + "[DEBUG] " + message);
        }
    }

    private static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}