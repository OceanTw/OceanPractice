package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.match.MatchManager;
import lol.oce.marine.match.MatchType;
import lol.oce.marine.match.modes.TeamVersusTeamMatch;
import lol.oce.marine.players.User;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("practice.admin")) {
            return false;
        }

        if (strings.length == 0) {
            return false;
        }

        Practice.getInstance().getMatchManager().startTeam(MatchType.DUEL, Practice.getInstance().getKitManager().getKit("boxing"), new User[]{}, new User[]{}, false);
        return false;
    }
}
