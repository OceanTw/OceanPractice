package lol.oce.vpractice.listeners;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.gui.Menu;
import lol.oce.vpractice.gui.impl.QueueMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    Menu menu = new Menu();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getLobbyItemManager().getQueueItemName())) {
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
}
