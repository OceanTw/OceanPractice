package lol.oce.hercules.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static String serialize(ItemStack item) {
        // Serialize an inventory
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            builder.append(item.getType().name()).append(":").append(item.getAmount()).append(":");
            item.getEnchantments().forEach((enchantment, level) -> {
                builder.append(enchantment.getName()).append(":").append(level).append(",");
            });
            // Remove the trailing comma if there are enchantments
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
        }
        return builder.toString();
    }

    public static ItemStack deserialize(String serialized) {
        // Deserialize an inventory
        String[] parts = serialized.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid serialized item format: " + serialized);
        }
        Material material = Material.getMaterial(parts[0]);
        if (material == null) {
            throw new IllegalArgumentException("Invalid material in serialized item: " + parts[0]);
        }
        int amount;
        try {
            amount = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount in serialized item: " + parts[1], e);
        }
        ItemStack item = new ItemStack(material, amount);
        if (parts.length > 2) {
            for (int i = 2; i < parts.length; i += 2) {
                if (i + 1 >= parts.length || parts[i].isEmpty()) {
                    continue; // Skip invalid or empty enchantment parts
                }
                Enchantment enchantment = Enchantment.getByName(parts[i]);
                if (enchantment == null) {
                    throw new IllegalArgumentException("Invalid enchantment in serialized item: " + parts[i]);
                }
                int level;
                try {
                    level = Integer.parseInt(parts[i + 1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid enchantment level in serialized item: " + parts[i + 1], e);
                }
                item.addEnchantment(enchantment, level);
            }
        }
        return item;
    }
}