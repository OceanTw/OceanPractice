package lol.oce.marine.gui.impl;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Gui;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.StringUtils;
import lol.oce.marine.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class UserMenu extends Gui {
    @Override
    public Inventory getInventory(Player player, Object... args) {
        User user = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
        Inventory inventory = Practice.getInstance().getServer().createInventory(null, 54, "User: " + user.getPlayer().getName());

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
        inventory.setItem(13, item);

        ItemStack elo = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta eloMeta = elo.getItemMeta();
        eloMeta.setDisplayName(StringUtils.handle("&b&lElo"));
        List<String> eloLore = new ArrayList<>();
        if (user.getKitStats().getElo().isEmpty()) {
            eloLore.add(StringUtils.handle("&fNo stats available"));
        } {
            for (Kit kit : user.getKitStats().getElo().keySet()) {
                eloLore.add(StringUtils.handle("&f" + kit.getName() + ": &b" + user.getKitStats().getElo().get(kit)));
            }
        }
        eloMeta.setLore(eloLore);
        elo.setItemMeta(eloMeta);
        inventory.setItem(29, elo);

        ItemStack wins = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta winsMeta = wins.getItemMeta();
        winsMeta.setDisplayName(StringUtils.handle("&b&lWins"));
        List<String> winsLore = new ArrayList<>();
        if (user.getKitStats().getWins().isEmpty()) {
            winsLore.add(StringUtils.handle("&fNo stats available"));
        } else {for (Kit kit : user.getKitStats().getWins().keySet()) {
                winsLore.add(StringUtils.handle("&f" + kit.getName() + ": &b" + user.getKitStats().getWins().get(kit)));
            }
        }
        winsMeta.setLore(winsLore);
        wins.setItemMeta(winsMeta);
        inventory.setItem(31, wins);

        ItemStack losses = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta lossesMeta = losses.getItemMeta();
        lossesMeta.setDisplayName(StringUtils.handle("&b&lLosses"));
        List<String> lossesLore = new ArrayList<>();
        if (user.getKitStats().getLosses().isEmpty()) {
            lossesLore.add(StringUtils.handle("&fNo stats available"));
        } else {
            for (Kit kit : user.getKitStats().getLosses().keySet()) {
                lossesLore.add(StringUtils.handle("&f" + kit.getName() + ": &b" + user.getKitStats().getLosses().get(kit)));
            }
        }
        lossesMeta.setLore(lossesLore);
        losses.setItemMeta(lossesMeta);
        inventory.setItem(33, losses);

        ItemStack wipe = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta wipeMeta = wipe.getItemMeta();
        wipeMeta.setDisplayName(StringUtils.handle("&c&lReset Stats for " + user.getPlayer().getName()));
        List<String> wipeLore = new ArrayList<>();
        wipeLore.add(StringUtils.handle("&7"));
        wipeLore.add(StringUtils.handle("&cWARNING: This will reset all stats from this user,"));
        wipeLore.add(StringUtils.handle("&cThis action cannot be undone."));
        wipeLore.add(StringUtils.handle("&7"));
        wipeLore.add(StringUtils.handle("&4&lClick to wipe stats"));
        wipeMeta.setLore(wipeLore);
        wipe.setItemMeta(wipeMeta);
        inventory.setItem(40, wipe);

        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(StringUtils.handle("&b&lBack to User Management"));
        back.setItemMeta(backMeta);
        inventory.setItem(45, back);

        return inventory;
    }
}
