package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import lol.oce.hercules.gui.Menu;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    Menu menu = new Menu();

    @EventHandler
    public void onItemRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (event.getItem() == null || event.getItem().getItemMeta() == null) {
            return;
        }

        if (event.getItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&9&l1v1 Queue &7(Right Click)"))) {
            menu.getQueueMenu().open(event.getPlayer());
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getLobbyItemManager().getCreatePartyName())) {
            // TODO: Implement party system
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getLobbyItemManager().getSettingsName())) {
            // TODO: Implement settings UI
        }
    }

    @EventHandler
    public void onQueueMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals("Select your queue")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(StringUtils.handle("&9&lUnranked"))) {
                    menu.getQueueUnrankedMenu().open(player);
                    event.setCancelled(true);
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&9&lRanked"))) {
                    player.sendMessage(StringUtils.handle("&7&oYou have selected the ranked queue"));
                    player.closeInventory();
                    event.setCancelled(true);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQueueKitMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals("Unranked Queue")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&9&oYou are now queueing a match with the " + kit.getDisplayName() + " kit"));
                    Practice.getQueueManager().joinQueue(Practice.getUserManager().getUser(player.getUniqueId()), kit, false);
                    player.closeInventory();
                    event.setCancelled(true);
                }
            }
            event.setCancelled(true);
        } else if (event.getInventory().getName().equals("Ranked Queue")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&9&oYou are now queueing a Ranked match with the " + kit.getDisplayName() + " kit"));
                    Practice.getQueueManager().joinQueue(Practice.getUserManager().getUser(player.getUniqueId()), kit, true);
                    player.closeInventory();
                    event.setCancelled(true);
                }
            }
        } else if (event.getInventory().getName().contains("Duel Request to ")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                if (kit != null) {
                    String targetName = event.getInventory().getName().replace("Duel Request to ", "");
                    User target = Practice.getUserManager().getUser(Bukkit.getPlayer(targetName).getUniqueId());
                    player.sendMessage(StringUtils.handle("&9&oYou have sent a duel request"));
                    Practice.getRequestManager().sendRequest(Practice.getUserManager().getUser(player.getUniqueId()), target, kit);
                    player.closeInventory();
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.getPlayer().sendMessage(StringUtils.handle("&cYou cannot drop items in your current stage."));
        event.setCancelled(true);
    }

}
