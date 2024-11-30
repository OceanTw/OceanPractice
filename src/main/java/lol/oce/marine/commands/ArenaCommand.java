package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.arenas.ArenaType;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!sender.hasPermission("star.admin")) {
            sender.sendMessage("You do not have permission to execute this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage("/arena create <name> <displayname> <type>");
            player.sendMessage("/arena set <red|blue|c1|c2> <name>");
            return true;
        }

        String subCommand = args[0];
        String arenaName = args[1];

        switch (subCommand.toLowerCase()) {
            case "create":
                if (args.length < 4) {
                    player.sendMessage("Usage: /arena create <name> <displayname> <type>");
                    return true;
                }
                String displayName = args[2];
                ArenaType type;
                if (args[3].equals("SHARED")) {
                    type = ArenaType.SHARED;
                } else {
                    type = ArenaType.STANDALONE;
                }
                Arena arena = new Arena(arenaName, displayName, type, true, null, null, null, null);
                Practice.getArenaManager().addArena(arena);
                arena.save();
                player.sendMessage("Arena " + displayName + " created with the type " + type.name() + ". Now set the locations using /arena set <red|blue|c1|c2>.");
                break;

            case "set":
                if (args.length != 3) {
                    player.sendMessage("Usage: /arena set <red|blue|c1|c2> <name>");
                    return true;
                }
                Arena setupArena = Practice.getArenaManager().getArena(arenaName);
                if (setupArena == null) {
                    player.sendMessage("No arena found with the name " + arenaName + ". Create it first using /arena create.");
                    return true;
                }
                Location location = player.getLocation();
                switch (args[2].toLowerCase()) {
                    case "red":
                        setupArena.setRedSpawn(location);
                        player.sendMessage("Red spawn set.");
                        break;
                    case "blue":
                        setupArena.setBlueSpawn(location);
                        player.sendMessage("Blue spawn set.");
                        break;
                    case "c1":
                        setupArena.setCorner1(location);
                        player.sendMessage("Corner 1 set.");
                        break;
                    case "c2":
                        setupArena.setCorner2(location);
                        player.sendMessage("Corner 2 set.");
                        break;
                    default:
                        player.sendMessage("Invalid location type. Use red, blue, c1, or c2.");
                        return true;
                }
                if (setupArena.getRedSpawn() != null && setupArena.getBlueSpawn() != null && setupArena.getCorner1() != null && setupArena.getCorner2() != null) {
                    Practice.getArenaManager().addArena(setupArena);
                    setupArena.save();
                    player.sendMessage("Arena " + setupArena.getDisplayName() + " setup complete and saved.");
                }
                break;

            default:
                player.sendMessage("Invalid subcommand. Use create or set.");
                break;
        }
        return true;
    }
}