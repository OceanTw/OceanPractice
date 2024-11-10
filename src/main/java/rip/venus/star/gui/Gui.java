package rip.venus.star.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class Gui {
    public void open(Player player, Object... args) {
        player.openInventory(getInventory(player));
    }

    public abstract Inventory getInventory(Player player, Object... args);
}
