package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.players.User;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("practice.admin")) {
            return false;
        }

        if (strings.length == 0) {
            return false;
        }
        Player player = (Player) commandSender;
        User user = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
        switch (strings[0]) {
            case "setspawn":
                // Set the spawn
                Practice.getInstance().getLobbyManager().setSpawn(player.getLocation());
                player.sendMessage(StringUtils.handle("&aSpawn location set!"));
                break;
            case "leavequeue":
                // Leave the queue
                Practice.getInstance().getQueueManager().leaveQueue(user);
                break;
            case "reload":
                // Reload the config
                Practice.getInstance().getConfigService().reload();
                break;
            default:
                break;
        }

        return false;
    }
}
