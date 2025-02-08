package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Menu;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserStatus;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    Menu menu = new Menu();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        User user = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() != UserStatus.IN_LOBBY) {
            player.sendMessage(StringUtils.handle("&cYou are not in the lobby!"));
            return false;
        }

        if (strings.length != 1) {
            player.sendMessage(StringUtils.handle("&cUsage: /duel <player>"));
            return false;
        }

        Player target = Practice.getInstance().getInstance().getServer().getPlayer(strings[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(StringUtils.handle("&cPlayer not found!"));
            return false;
        }

        User targetUser = Practice.getInstance().getUserManager().getUser(target.getUniqueId());

        if (targetUser.getStatus() != UserStatus.IN_LOBBY) {
            player.sendMessage(StringUtils.handle("&cThat player is not in the lobby!"));
            return false;
        }

        if (targetUser.equals(user)) {
            player.sendMessage(StringUtils.handle("&cYou cannot duel yourself!"));
            return false;
        }

        menu.getDuelMenu().open(player, target.getName());
        return false;
    }
}
