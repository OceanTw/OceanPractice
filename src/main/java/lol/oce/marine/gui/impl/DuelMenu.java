package lol.oce.marine.gui.impl;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Gui;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.kits.KitManager;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DuelMenu extends Gui {
    @Override
    public Inventory getInventory(Player player, Object... args) {
        Inventory duel = Bukkit.createInventory(player, 27, "Duel Request to " + args[0]);

        for (int i = 0; i < 27; i++) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtils.handle("&7 "));
            duel.setItem(i, item);
        }

        KitManager kitManager = Practice.getInstance().getKitManager();
        List<Kit> kits = kitManager.getEnabledKits();
        int[] slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43};
        if (kitManager.getEnabledKits().size() > 27) {
            // TODO: Handle more than 27 kits
            return duel;
        }
        for (int i = 0; i < slots.length; i++) {
            if (i < Practice.getInstance().getKitManager().getEnabledKits().size()) {
                // TODO: Show current in queue amount
                Kit kit = Practice.getInstance().getKitManager().getEnabledKits().get(i);
                ItemStack item = new ItemStack(kit.getIcon());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(StringUtils.handle("&b&l" + kit.getDisplayName()));
                List<String> lore = new ArrayList<>();
                lore.add(StringUtils.handle("&7"));
                lore.add(StringUtils.line("&7"));
                lore.add(StringUtils.handle("&f" + kit.getDescription()));
                lore.add(StringUtils.line("&7"));
                lore.add(StringUtils.handle("&7"));
                meta.setLore(lore);
                item.setItemMeta(meta);
                duel.setItem(slots[i], item);
            }
        }
        return duel;
    }
}
