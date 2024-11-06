package lol.oce.hercules.commands;

import lol.oce.hercules.Practice;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("hercules.admin")) {
            return false;
        }

        if (strings.length == 0) {
            return false;
        }
        Player player = (Player) commandSender;
        User user = UserManager.getUser(player.getUniqueId());
        switch (strings[0]) {
            case "leavequeue":
                // Leave the queue
                Practice.getQueueManager().leaveQueue(user);
                break;
            default:
                break;
        }

        return false;
    }
}
