package lol.oce.marine.gui.impl;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Gui;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.kits.KitManager;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.utils.StringUtils;
import lol.oce.marine.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class UserManagementMenu extends Gui {


    Inventory userManagementMenu = Bukkit.createInventory(null, 54, "User Management");

    @Override
    public Inventory getInventory(Player player, Object... args) {
        for (int i = 0; i < 9; i++) {
            setBorderItem(i);
            setBorderItem(45 + i);
        }
        String filter = args.length > 0 ? (String) args[0] : null;

        int page = args.length > 0 ? (Integer) args[1] : 1;
        if (page > 1) {
            ItemStack previousPage = new ItemStack(Material.ARROW, 1);
            ItemMeta previousPageMeta = previousPage.getItemMeta();
            previousPageMeta.setDisplayName(StringUtils.handle("&bPrevious Page"));
            previousPage.setItemMeta(previousPageMeta);
            userManagementMenu.setItem(45, previousPage);
        }
        // TODO: Check if there are more users
        ItemStack nextPage = new ItemStack(Material.ARROW, 1);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName(StringUtils.handle("&bNext Page"));
        nextPage.setItemMeta(nextPageMeta);
        userManagementMenu.setItem(53, nextPage);

        ItemStack search = new ItemStack(Material.COMPASS, 1);
        ItemMeta searchMeta = search.getItemMeta();
        searchMeta.setDisplayName(StringUtils.handle("&bSearch for a user"));
        search.setItemMeta(searchMeta);
        userManagementMenu.setItem(49, search);

        // Get the first 28 users
        UserManager userManager = Practice.getInstance().getUserManager();
        List<User> users;
        if (filter != null) {
            users = userManager.getUsers(filter);
        } else {
            users = userManager.getUsers((page - 1) * 28, 28);
        }
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(Bukkit.getOfflinePlayer(user.getUuid()).getName());
            item.setItemMeta(skullMeta);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(StringUtils.handle("&b&l" + Bukkit.getOfflinePlayer(user.getUuid()).getName()));
            List<String> lore = new ArrayList<>();
            lore.add(StringUtils.handle("&fUUID: &b" + user.getUuid().toString()));
            lore.add(StringUtils.handle("&fStatus: &b" + user.getStatus().getDisplayName()));
            lore.add(StringUtils.handle("&7"));
            if (user.getMatch() != null) {
                lore.add(StringUtils.handle("&bMatch:"));
                lore.add(StringUtils.handle("&fType: &b" + user.getMatch().getType().name()));
                lore.add(StringUtils.handle("&fArena: &b" + user.getMatch().getArena().getDisplayName()));
                lore.add(StringUtils.handle("&fKit: &b" + user.getMatch().getKit().getName()));
                lore.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(user.getMatch().getTime())));
                lore.add(StringUtils.handle("&7"));
            }
            // TODO: Party
            lore.add(StringUtils.handle("&b&lClick to view more"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            userManagementMenu.setItem(i + 9, item);
        }

        return userManagementMenu;
    }

    private void setBorderItem(int index) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtils.handle("&7 "));
        item.setItemMeta(meta);
        userManagementMenu.setItem(index, item);
    }
}
