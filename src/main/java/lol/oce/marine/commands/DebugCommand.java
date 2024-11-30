package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.players.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("star.admin")) {
            return false;
        }

        if (strings.length == 0) {
            return false;
        }
        Player player = (Player) commandSender;
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        switch (strings[0]) {
            case "leavequeue":
                // Leave the queue
                Practice.getQueueManager().leaveQueue(user);
                break;
            case "reload":
                // Reload the config
                Practice.getSettingsConfig().reload();
                break;
            default:
                break;
        }

        return false;
    }
}
