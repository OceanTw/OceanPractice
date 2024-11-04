package lol.oce.hercules.gui;

import lol.oce.hercules.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Gui {
    public void open(Player player, Object... args) {
        player.sendMessage(StringUtils.handle("&7&oOpening GUI..."));
        player.openInventory(getInventory(player));
    }

    public Inventory getInventory(Player player, Object... args) {
        return Bukkit.createInventory(player, 9, "GUI");
    }
}
