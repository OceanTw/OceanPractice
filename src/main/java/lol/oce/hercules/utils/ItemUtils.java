package lol.oce.hercules.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static String serialize(ItemStack item) {
        // Serialize an inventory
        StringBuilder builder = new StringBuilder();
        if (item != null) {
            // get the enchantments
            builder.append(item.getType().name()).append(":").append(item.getAmount()).append(":");
            item.getEnchantments().forEach((enchantment, level) -> {
                builder.append(enchantment.getName()).append(":").append(level).append(",");
            });}
        return builder.toString();
    }

    public static ItemStack deserialize(String serialized) {
        // Deserialize an inventory
        String[] parts = serialized.split(":");
        Material material = Material.getMaterial(parts[0]);
        int amount = Integer.parseInt(parts[1]);
        ItemStack item = new ItemStack(material, amount);
        Enchantment enchantment = Enchantment.getByName(parts[2]);
        int level = Integer.parseInt(parts[3]);
        item.addEnchantment(enchantment, level);
        return item;
    }

}
