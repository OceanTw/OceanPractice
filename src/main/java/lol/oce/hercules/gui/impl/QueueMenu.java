package lol.oce.hercules.gui.impl;

import lol.oce.hercules.gui.Gui;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class QueueMenu extends Gui {

    @Override
    public Inventory getInventory(Player player, Object... args) {
        Inventory queue = Bukkit.createInventory(null, 27, "Select your queue");

        for (int i = 0; i < 27; i++) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtils.handle("&7 "));
            item.setItemMeta(meta);
            queue.setItem(i, item);
        }

        ItemStack unranked = new ItemStack(Material.IRON_SWORD);
        ItemMeta unrankedMeta = unranked.getItemMeta();
        unrankedMeta.setDisplayName(StringUtils.handle("&c&lUnranked"));
        List<String> unrankedLore = new ArrayList<>();
        unrankedLore.add(StringUtils.handle("&7"));
        unrankedLore.add(StringUtils.line("&7"));
        unrankedLore.add(StringUtils.handle("&fPractice against other players"));
        unrankedLore.add(StringUtils.handle("&fwithout losing or gaining rank"));
        unrankedLore.add(StringUtils.line("&7"));
        unrankedLore.add(StringUtils.handle("&7"));
        unrankedLore.add(StringUtils.handle("&c&lClick to view more options"));
        unrankedMeta.setLore(unrankedLore);
        unrankedMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        unranked.setItemMeta(unrankedMeta);

        ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta rankedMeta = ranked.getItemMeta();
        rankedMeta.setDisplayName(StringUtils.handle("&c&lRanked"));
        List<String> rankedLore = new ArrayList<>();
        rankedLore.add(StringUtils.handle("&7"));
        rankedLore.add(StringUtils.line("&7"));
        rankedLore.add(StringUtils.handle("&fClimb the ranks and compete"));
        rankedLore.add(StringUtils.handle("&fagainst other skilled players"));
        rankedLore.add(StringUtils.line("&7"));
        rankedLore.add(StringUtils.handle("&7"));
        rankedLore.add(StringUtils.handle("&c&lClick to view more options"));
        rankedMeta.setLore(rankedLore);
        rankedMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ranked.setItemMeta(rankedMeta);

        queue.setItem(11, unranked);
        queue.setItem(15, ranked);
        return queue;
    }
}
