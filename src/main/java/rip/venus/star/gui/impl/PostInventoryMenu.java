package rip.venus.star.gui.impl;

import rip.venus.star.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class PostInventoryMenu extends Gui {
    @Override
    public Inventory getInventory(Player player, Object... args) {
        Inventory postInventory = Bukkit.createInventory(null, 54, "Post Game Stats");

        for (int i = 40; i < 44; i++) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("&7 ");
            item.setItemMeta(meta);
        }

        for (int i = 0; i < 35; i++) {
            postInventory.setItem(i, player.getInventory().getItem(i));
        }

        postInventory.setItem(36, player.getInventory().getHelmet());
        postInventory.setItem(37, player.getInventory().getChestplate());
        postInventory.setItem(38, player.getInventory().getLeggings());
        postInventory.setItem(39, player.getInventory().getBoots());

        ItemStack stats = getStats(args);
        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        String nextPlayer = (String) args[6];
        nextMeta.setDisplayName("&cView " + nextPlayer + "'s Stats");
        nextMeta.setLore(Collections.singletonList(args[0].toString()));
        next.setItemMeta(nextMeta);

        postInventory.setItem(45, stats);
        return null;
    }

    private static ItemStack getStats(Object[] args) {
        ItemStack stats = new ItemStack(Material.BOOK);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName("&cStats");
        List<String> statsLore = statsMeta.getLore();
        statsLore.add("Hits: &c" + args[1]);
        statsLore.add("Blocked Hits: &c" + args[2]);
        statsLore.add("Damage Dealt: &c" + args[3]);
        statsLore.add("Damage Taken: &c" + args[4]);
        statsLore.add("Healed: &c" + args[5]);
        statsLore.add("Potions Thrown: &c" + args[6]);
        statsMeta.setLore(statsLore);
        stats.setItemMeta(statsMeta);
        return stats;
    }
}
