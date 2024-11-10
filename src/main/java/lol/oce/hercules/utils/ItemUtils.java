package lol.oce.hercules.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static String serialize(ItemStack item) {
        // Serialize an inventory
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            builder.append(item.getType().name()).append(":").append(item.getAmount());
            if (!item.getEnchantments().isEmpty()) {
                for (Enchantment enchantment : item.getEnchantments().keySet()) {
                    builder.append(":").append(enchantment.getName()).append(":").append(item.getEnchantmentLevel(enchantment));
                }
            }
            builder.append(";");
        }
        return builder.toString();
    }

    public static String serialize(List<ItemStack> items) {
        // Serialize an array of items
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }
            builder.append(serialize(item));
        }
        return builder.toString();
    }

    public static String serialize(ItemStack[] items) {
        // Serialize an array of items
        StringBuilder builder = new StringBuilder();
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }
            builder.append(serialize(item));
        }
        return builder.toString();
    }

    // TODO: Fix issues with enchanted items
    public static ItemStack deserialize(String serialized) {
        // Deserialize an item
        String[] parts = serialized.split(":");
        Material material = Material.getMaterial(parts[0]);
        int amount = Integer.parseInt(parts[1]);
        ItemStack item = new ItemStack(material, amount);
        if (parts.length > 2) {
            for (int i = 2; i < parts.length; i += 2) {
                Enchantment enchantment = Enchantment.getByName(parts[i]);
                int level = Integer.parseInt(parts[i + 1]);
                item.addEnchantment(enchantment, level);
            }
        }
        return item;
    }

    public static List<ItemStack> deserializeArray(String serialized) {
        // Deserialize an array of items
        List<ItemStack> items = new ArrayList<>();
        String[] parts = serialized.split(";");
        for (String part : parts) {
            items.add(deserialize(part));
        }
        return items;
    }

}