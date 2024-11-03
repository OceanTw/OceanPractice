package lol.oce.vpractice.listeners;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.gui.Menu;
import lol.oce.vpractice.gui.impl.QueueMenu;
import lol.oce.vpractice.utils.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    Menu menu = new Menu();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
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
    public void onMenuClick(InventoryClickEvent event) {
        // TODO: Open the queue menu
        if (event.getInventory().getName().equals("Select your queue")) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&9&lUnranked"))) {
                event.getWhoClicked().sendMessage(StringUtils.handle("&7&oYou have selected the unranked queue"));
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains(StringUtils.handle("&9&lRanked"))) {
                event.getWhoClicked().sendMessage(StringUtils.handle("&7&oYou have selected the ranked queue"));
                event.getWhoClicked().closeInventory();
                event.setCancelled(true);
            }
        }

    }

}
