package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Menu;
import lol.oce.marine.gui.impl.UserManagementMenu;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("practice.admin")) {
            return false;
        }
        new UserManagementMenu().open((Player) commandSender);
        return true;
    }
}
