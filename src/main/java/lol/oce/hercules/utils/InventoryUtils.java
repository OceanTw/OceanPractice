package lol.oce.hercules.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static String serialize(Inventory inventory) {
        // Serialize an inventory
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                builder.append(item.getType().name()).append(":").append(item.getAmount()).append(",");
            }
        }
        return builder.toString();
    }

    public static Inventory deserialize(String serialized) {
        // Deserialize an inventory
        Inventory inventory = null;
        for (String item : serialized.split(",")) {
            String[] parts = item.split(":");
            Material material = Material.getMaterial(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            inventory.addItem(new ItemStack(material, amount));
        }
        return inventory;
    }

}
