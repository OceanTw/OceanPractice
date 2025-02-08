package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.players.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            Player player = (Player) commandSender;
            User receiver = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
            User sender = Practice.getInstance().getUserManager().getUser(Bukkit.getPlayer(strings[0]).getUniqueId());
            if (Practice.getInstance().getRequestManager().getRequest(sender, receiver) != null) {
                Practice.getInstance().getRequestManager().acceptRequest(sender, receiver);
            }
        }
        return false;
    }
}
