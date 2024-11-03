package lol.oce.vpractice.commands;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.kits.KitManager;
import lol.oce.vpractice.utils.ConsoleUtils;
import lol.oce.vpractice.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("vpractice.admin")) {
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
            Kit kit = new Kit(strings[1], strings[1], "", null, null, null, true, true, false, false, false, false, false, false, false, false, false, false);
            Practice.getKitManager().addKit(kit);
            commandSender.sendMessage(StringUtils.handle("&aKit created successfully"));
            return true;
        }

        if (strings[0].equalsIgnoreCase("delete")) {
            if (length < 2) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Delete a kit
            KitManager kitManager = Practice.getKitManager();
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
            // Set the kit inventory
            Practice.getKitManager().getKit(strings[1]).setInventory(player.getInventory());
            return true;
        }

        if (strings[0].equalsIgnoreCase("set")) {
            if (length < 3) {
                commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                return true;
            }
            // Set the kit properties
            switch (strings[2]) {
                case "name":
                    Practice.getKitManager().getKit(strings[1]).setName(strings[3]);
                    break;
                case "displayname":
                    Practice.getKitManager().getKit(strings[1]).setDisplayName(strings[3]);
                    break;
                case "description":
                    Practice.getKitManager().getKit(strings[1]).setDescription(strings[3]);
                    break;
                case "enabled":
                    Practice.getKitManager().getKit(strings[1]).setEnabled(Boolean.parseBoolean(strings[3]));
                    break;
                case "editable":
                    Practice.getKitManager().getKit(strings[1]).setEditable(Boolean.parseBoolean(strings[3]));
                    break;
                case "boxing":
                    Practice.getKitManager().getKit(strings[1]).setBoxing(Boolean.parseBoolean(strings[3]));
                    break;
                case "build":
                    Practice.getKitManager().getKit(strings[1]).setBuild(Boolean.parseBoolean(strings[3]));
                    break;
                case "sumo":
                    Practice.getKitManager().getKit(strings[1]).setSumo(Boolean.parseBoolean(strings[3]));
                    break;
                case "mapdestroyable":
                    Practice.getKitManager().getKit(strings[1]).setMapDestroyable(Boolean.parseBoolean(strings[3]));
                    break;
                case "hunger":
                    Practice.getKitManager().getKit(strings[1]).setHunger(Boolean.parseBoolean(strings[3]));
                    break;
                case "healthregen":
                    Practice.getKitManager().getKit(strings[1]).setHealthRegen(Boolean.parseBoolean(strings[3]));
                    break;
                case "bedfight":
                    Practice.getKitManager().getKit(strings[1]).setBedfight(Boolean.parseBoolean(strings[3]));
                    break;
                case "fireball":
                    Practice.getKitManager().getKit(strings[1]).setFireball(Boolean.parseBoolean(strings[3]));
                    break;
                case "enderpearlcd":
                    Practice.getKitManager().getKit(strings[1]).setEnderpearlcd(Boolean.parseBoolean(strings[3]));
                    break;
                case "ranked":
                    Practice.getKitManager().getKit(strings[1]).setRanked(Boolean.parseBoolean(strings[3]));
                    break;
                default:
                    commandSender.sendMessage(StringUtils.handle("&cInvalid usage"));
                    return true;
            }
            Practice.getKitManager().updateSettings();
            ConsoleUtils.info("Kit settings updated");
            return true;
        }
        return false;
    }
}
