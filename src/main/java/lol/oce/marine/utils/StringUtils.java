package lol.oce.marine.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class StringUtils {
    public static String handle(String message, String... params) {

        if (params == null) {
            return message;
        }

        for (int i = 0; i < params.length; i += 2) {
            message = message.replace(params[i], params[i + 1]);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String handle(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String line(String color) {
        return handle(color + "&m-------------------------");
    }

    public static String line(String color, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append("-");
        }
        return handle(color + "&m" + builder.toString());
    }
}