package lol.oce.marine.listeners;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.impl.UserManagementMenu;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.players.User;
import lol.oce.marine.utils.StringUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class MenuListener implements Listener {

    @EventHandler
    public void onQueueKitMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
            event.setCancelled(true);
            return;
        }
        if (event.getInventory().getName().equals("Unranked Queue")) {
            player.closeInventory();
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getInstance().getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&b&oYou are now queueing a match with the " + kit.getDisplayName() + " kit"));
                    Practice.getInstance().getQueueManager().joinQueue(Practice.getInstance().getUserManager().getUser(player.getUniqueId()), kit, false);
                }
            }
            event.setCancelled(true);
        } else if (event.getInventory().getName().equals("Ranked Queue")) {
            player.closeInventory();
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getInstance().getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&b&oYou are now queueing a Ranked match with the " + kit.getDisplayName() + " kit"));
                    Practice.getInstance().getQueueManager().joinQueue(Practice.getInstance().getUserManager().getUser(player.getUniqueId()), kit, true);
                }
            }
        } else if (event.getInventory().getName().contains("Duel Request to ")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getInstance().getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    String targetName = event.getInventory().getName().replace("Duel Request to ", "");
                    User target = Practice.getInstance().getUserManager().getUser(Bukkit.getPlayer(targetName).getUniqueId());
                    player.sendMessage(StringUtils.handle("&b&oYou have sent a duel request"));
                    Practice.getInstance().getRequestManager().sendRequest(Practice.getInstance().getUserManager().getUser(player.getUniqueId()), target, kit);
                }
            }
        }
    }

    @EventHandler
    public void onUserManagementMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
            event.setCancelled(true);
            return;
        }

        if (event.getInventory().getName().equals("User Management")) {
            player.closeInventory();
            event.setCancelled(true);
            String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if (name.contains("Search for a user")) {
//                openSearch(player);
                return;
            }
            player.sendMessage(StringUtils.handle("&b&oOpened management menu of " + name));
        }
    }

}