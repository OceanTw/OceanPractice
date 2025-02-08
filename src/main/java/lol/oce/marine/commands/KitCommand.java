package lol.oce.marine.commands;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.kits.KitManager;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("practice.admin")) {
            commandSender.sendMessage(StringUtils.handle("&cYou do not have permission to execute this command."));
            return true;
        }
        Player player = (Player) commandSender;
        int length = strings.length;

        if (length == 0) {
            commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("create")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Create a new kit
            Practice.getInstance().getKitManager().createKit(strings[1]);
            commandSender.sendMessage(StringUtils.handle("&aKit created successfully"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("delete")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Delete a kit
            KitManager kitManager = Practice.getInstance().getKitManager();
            Kit kit = kitManager.getKit(strings[1]);
            if (kit == null) {
                commandSender.sendMessage(StringUtils.handle("&cKit not found"));
                return true;
            }
            kitManager.removeKit(kit);
            return true;
        }

        if (strings[0].equalsIgnoreCase("setinv")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }

            if (Practice.getInstance().getKitManager().getKit(strings[1]) == null) {
                commandSender.sendMessage(StringUtils.handle("&cKit not found"));
                return true;
            }
            // Set the kit inventory
            Kit kit = Practice.getInstance().getKitManager().getKit(strings[1]);
            Practice.getInstance().getKitManager().setKitInventory(kit, player);
            player.sendMessage(StringUtils.handle("&aKit inventory set successfully"));
            kit.save();
            return true;
        }

        if (strings[0].equalsIgnoreCase("addarena")) {
            Arena arena = Practice.getInstance().getArenaManager().getArena(strings[2]);
            if (arena == null) {
                commandSender.sendMessage(StringUtils.handle("&cArena not found"));
                return true;
            }
            if (Practice.getInstance().getKitManager().getKit(strings[1]) == null) {
                commandSender.sendMessage(StringUtils.handle("&cKit not found"));
                return true;
            }

            Practice.getInstance().getKitManager().getKit(strings[1]).addArena(arena);
            player.sendMessage(StringUtils.handle("&aArena added to kit successfully"));
            Practice.getInstance().getArenaManager().save();
        }

        if (strings[0].equalsIgnoreCase("set")) {
            if (length < 3) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }

            if (Practice.getInstance().getKitManager().getKit(strings[1]) == null) {
                commandSender.sendMessage(StringUtils.handle("&cKit not found"));
                return true;
            }
            // Set the kit properties
            switch (strings[2]) {
                case "icon":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setIcon(player.getItemInHand().getType());
                    player.sendMessage(StringUtils.handle("&aKit icon set successfully"));
                    break;
                case "name":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setName(strings[3]);
                    player.sendMessage(StringUtils.handle("&aKit name set successfully"));
                    break;
                case "displayname":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setDisplayName(strings[3]);
                    player.sendMessage(StringUtils.handle("&aKit display name set successfully"));
                    break;
                case "description":
                    StringBuilder description = new StringBuilder();
                    for (int i = 3; i < strings.length; i++) {
                        description.append(strings[i]).append(" ");
                    }
                    Practice.getInstance().getKitManager().getKit(strings[1]).setDescription(description.toString());
                    player.sendMessage(StringUtils.handle("&aKit description set successfully"));
                    break;
                case "enabled":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setEnabled(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit enabled set successfully"));
                    break;
                case "editable":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setEditable(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit editable set successfully"));
                    break;
                case "boxing":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setBoxing(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit boxing set successfully"));
                    break;
                case "build":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setBuild(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit build set successfully"));
                    break;
                case "sumo":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setSumo(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit sumo set successfully"));
                    break;
                case "mapdestroyable":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setMapDestroyable(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit map destroyable set successfully"));
                    break;
                case "hunger":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setNoHunger(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit hunger set successfully"));
                    break;
                case "healthregen":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setHealthRegen(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit health regen set successfully"));
                    break;
                case "bedfight":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setBedFight(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit bedfight set successfully"));
                    break;
                case "fireball":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setFireball(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit fireball set successfully"));
                    break;
                case "enderpearlcd":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setEnderPearlCd(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit enderpearlcd set successfully"));
                    break;
                case "ranked":
                    Practice.getInstance().getKitManager().getKit(strings[1]).setRanked(Boolean.parseBoolean(strings[3]));
                    player.sendMessage(StringUtils.handle("&aKit ranked set successfully"));
                    break;
                default:
                    commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                    return true;
            }
            Practice.getInstance().getKitManager().getKit(strings[1]).save();
            return true;
        }
        return false;
    }
}
