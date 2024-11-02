package lol.oce.vpractice.match;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.players.User;
import lol.oce.vpractice.players.UserStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MatchListeners implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            if (!user.getMatch().isStarted()) {
                event.setCancelled(true);
            }
        }
    }
}
