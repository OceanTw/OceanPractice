package lol.oce.hercules.commands;

import lol.oce.hercules.Practice;
import lol.oce.hercules.gui.Menu;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    Menu menu = new Menu();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() != UserStatus.IN_LOBBY) {
            player.sendMessage(StringUtils.handle("&cYou are not in the lobby!"));
            return false;
        }

        if (strings.length != 1) {
            player.sendMessage(StringUtils.handle("&cUsage: /duel <player>"));
            return false;
        }

        Player target = Practice.getInstance().getServer().getPlayer(strings[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(StringUtils.handle("&cPlayer not found!"));
            return false;
        }

        User targetUser = Practice.getUserManager().getUser(target.getUniqueId());

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
