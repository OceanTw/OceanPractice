package lol.oce.marine.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import lol.oce.marine.Practice;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
    public void onPot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof ThrownPotion)) return;
        Entity entity = (Entity) event.getEntity().getShooter();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        User user = Practice.getUserManager().getUser(player.getUniqueId());

        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().onPotThrow(event);
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
    public void onPlayerHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            if (!user.getMatch().getKit().isNoHunger()) {
                event.setCancelled(false);
                return;
            }
        }
        event.setCancelled(true);
    }
}
