package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.ArenaManager;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DuplicateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (Practice.getInstance().getArenaManager().getArena(strings[0]) == null) {
            commandSender.sendMessage(StringUtils.handle("&cAn arena with that name does not exists!"));
            return false;
        }
        ArenaManager arenaManager = Practice.getInstance().getArenaManager();
        // name, amount, x, z
        arenaManager.duplicate(arenaManager.getArena(strings[0]), Integer.parseInt(strings[1]), Integer.parseInt(strings[2]), Integer.parseInt(strings[3]));
        return false;
    }
}
