package lol.oce.vpractice.listeners;

import lol.oce.vpractice.Practice;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem().getItemMeta().getDisplayName().equals(
                Practice.getLobbyItemManager().getQueueItemName())) {
            // TODO: Implement queue UI
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
