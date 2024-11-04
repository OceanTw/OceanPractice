package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Practice.getLobbyManager().giveItems(player);
        Practice.getUserManager().load(player.getUniqueId());
    }
}
