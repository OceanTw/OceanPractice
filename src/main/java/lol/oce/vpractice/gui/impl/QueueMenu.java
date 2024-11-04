package lol.oce.vpractice.gui.impl;

import lol.oce.vpractice.gui.Gui;
import lol.oce.vpractice.listeners.PlayerListener;
import lol.oce.vpractice.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
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
            queue.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        }

        ItemStack unranked = new ItemStack(Material.IRON_SWORD);
        ItemMeta unrankedMeta = unranked.getItemMeta();
        unrankedMeta.setDisplayName(StringUtils.handle("&9&lUnranked"));
        List<String> unrankedLore = new ArrayList<>();
        unrankedLore.add(StringUtils.handle("&7"));
        unrankedLore.add(StringUtils.line("&7"));
        unrankedLore.add(StringUtils.handle("&fPractice against other players"));
        unrankedLore.add(StringUtils.handle("&fwithout losing or gaining rank"));
        unrankedLore.add(StringUtils.line("&7"));
        unrankedLore.add(StringUtils.handle("&7"));
        unrankedLore.add(StringUtils.handle("&b&lClick to view more options"));
        unrankedMeta.setLore(unrankedLore);
        unrankedMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        unranked.setItemMeta(unrankedMeta);

        ItemStack ranked = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta rankedMeta = ranked.getItemMeta();
        rankedMeta.setDisplayName(StringUtils.handle("&9&lRanked"));
        List<String> rankedLore = new ArrayList<>();
        rankedLore.add(StringUtils.handle("&7"));
        rankedLore.add(StringUtils.line("&7"));
        rankedLore.add(StringUtils.handle("&fClimb the ranks and compete"));
        rankedLore.add(StringUtils.handle("&fagainst other skilled players"));
        rankedLore.add(StringUtils.line("&7"));
        rankedLore.add(StringUtils.handle("&7"));
        rankedLore.add(StringUtils.handle("&b&lClick to view more options"));
        rankedMeta.setLore(rankedLore);
        rankedMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ranked.setItemMeta(rankedMeta);

        queue.setItem(11, unranked);
        queue.setItem(15, ranked);
        return queue;
    }
}
