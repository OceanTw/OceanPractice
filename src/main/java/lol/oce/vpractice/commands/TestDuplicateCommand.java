package lol.oce.vpractice.commands;

import lol.oce.vpractice.arenas.Arena;
import lol.oce.vpractice.Practice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestDuplicateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!sender.getName().equals("WhatOcean")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage("Usage: /testduplicate <arenaName>");
            return true;
        }

        String arenaName = args[0];
        Arena arena = Practice.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage("Arena not found.");
            return true;
        }

        arena.duplicate();
        player.sendMessage("Arena duplicated successfully.");
        return true;
    }
}