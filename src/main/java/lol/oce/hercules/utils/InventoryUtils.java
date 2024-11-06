package lol.oce.hercules.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public static String serialize(Inventory inventory) {
        // Serialize an inventory
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : inventory.getContents()) {
            String serialized = ItemUtils.serialize(item);
            if (item == null) {
                continue;
            }
            if (item.getType() == Material.AIR) {
                continue;
            }
            if (!serialized.isEmpty()) {
                builder.append(serialized).append(",");
            }
        }
        return builder.toString();
    }

    public static Inventory deserialize(String serialized) {
        // Deserialize an inventory
        Inventory inventory = Bukkit.createInventory(null, 36);
        String[] parts = serialized.split(",");
        for (String part : parts) {
            ItemStack item = ItemUtils.deserialize(part);
            inventory.addItem(item);
        }
        return inventory;
    }
}
