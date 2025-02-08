package lol.oce.marine.listeners;

import lol.oce.marine.Practice;
import lol.oce.marine.gui.Menu;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.lobby.LobbyManager;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserStatus;
import lol.oce.marine.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
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

        if (event.getItem().getItemMeta().getDisplayName().equals(Practice.getInstance().getLobbyItemManager().getQueueItemName())) {
            menu.getQueueMenu().open(event.getPlayer());
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getInstance().getLobbyItemManager().getCreatePartyName())) {
            // TODO: Implement party system
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getInstance().getLobbyItemManager().getSettingsName())) {
            // TODO: Implement settings UI
        }

        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getInstance().getLobbyItemManager().getLeaveQueueName())) {
            User user = Practice.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId());
            if (user.getStatus() == UserStatus.IN_QUEUE) {
                Practice.getInstance().getQueueManager().leaveQueue(user);
                event.getPlayer().sendMessage(StringUtils.handle("&c&oYou have left the queue"));
            }
        }
    }

    @EventHandler
    public void onItemInventoryClick(InventoryClickEvent event) {
        if (Practice.getInstance().getUserManager().getUser(event.getWhoClicked().getUniqueId()).getStatus() != UserStatus.IN_MATCH) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQueueMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals("Select your queue")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(StringUtils.handle("&b&lUnranked"))) {
                    menu.getQueueUnrankedMenu().open(player);
                    event.setCancelled(true);
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&b&lRanked"))) {
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
    public void onItemDrop(PlayerDropItemEvent event) {
        if (Practice.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId()).getStatus() != UserStatus.IN_MATCH) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (Practice.getInstance().getUserManager().getUser(((Player) event.getDestination().getHolder()).getUniqueId()).getStatus() != UserStatus.IN_MATCH) {
            event.setCancelled(true);
        }
    }

}
