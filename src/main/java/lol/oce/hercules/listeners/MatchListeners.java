package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.ConsoleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MatchListeners implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().onMove(event);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().onBlockBreak(event);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().onBlockPlace(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            User user = Practice.getUserManager().getUser(player.getUniqueId());
            if (user.getStatus() == UserStatus.IN_MATCH) {
                user.getMatch().onHit(event);
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().onEat(event);
        }
    }
}
