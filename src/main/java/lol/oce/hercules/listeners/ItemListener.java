package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import lol.oce.hercules.gui.Menu;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.lobby.LobbyItemManager;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.utils.ConsoleUtils;
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

        if (event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(Practice.getLobbyItemManager().getQueueItemName())) {
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
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(StringUtils.handle("&5&lUnranked"))) {
                    menu.getQueueUnrankedMenu().open(player);
                    event.setCancelled(true);
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&5&lRanked"))) {
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
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&5&oYou are now queueing a match with the " + kit.getDisplayName() + " kit"));
                    Practice.getQueueManager().joinQueue(UserManager.getUser(player.getUniqueId()), kit, false);
                }
            }
            event.setCancelled(true);
        } else if (event.getInventory().getName().equals("Ranked Queue")) {
            player.closeInventory();
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    player.sendMessage(StringUtils.handle("&5&oYou are now queueing a Ranked match with the " + kit.getDisplayName() + " kit"));
                    Practice.getQueueManager().joinQueue(UserManager.getUser(player.getUniqueId()), kit, true);
                }
            }
        } else if (event.getInventory().getName().contains("Duel Request to ")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                Kit kit = Practice.getKitManager().getKitByDisplayName(event.getCurrentItem().getItemMeta().getDisplayName());
                player.closeInventory();
                if (kit != null) {
                    String targetName = event.getInventory().getName().replace("Duel Request to ", "");
                    User target = UserManager.getUser(Bukkit.getPlayer(targetName).getUniqueId());
                    player.sendMessage(StringUtils.handle("&5&oYou have sent a duel request"));
                    Practice.getRequestManager().sendRequest(UserManager.getUser(player.getUniqueId()), target, kit);
                }
            }
        }
    }


    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

}
