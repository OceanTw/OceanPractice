package lol.oce.marine.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public abstract class Gui implements Listener {
    public void open(Player player, Object... args) {
        player.openInventory(getInventory(player, args));
    }

    public abstract Inventory getInventory(Player player, Object... args);
}
