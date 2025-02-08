package lol.oce.marine.listeners;

import lol.oce.marine.Practice;
import lol.oce.marine.match.Participant;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.players.UserStatus;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.VisualUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Practice.getInstance().getLobbyManager().giveItems(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!Practice.isPluginLoaded()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is still loading");
            return;
        }
        Practice.getInstance().getUserManager().load(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        // if the server is not loaded, don't load the user
        Practice.getInstance().getUserManager().unload(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (Practice.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId()).getStatus() != UserStatus.IN_MATCH) {
            if (event.getPlayer().getGameMode() != org.bukkit.GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        User user = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
        User damager;
        if (user.getStatus() != UserStatus.IN_MATCH) {
            event.setCancelled(true);
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (!(entityEvent.getDamager() instanceof Player)) {
                return;
            }
            damager = Practice.getInstance().getUserManager().getUser((entityEvent.getDamager()).getUniqueId());
            if (damager.getStatus() != UserStatus.IN_MATCH) {
                event.setCancelled(true);
                return;
            }
            Participant killer = damager.getMatch().getParticipant(damager);
            Participant killed = user.getMatch().getParticipant(user);
            if (damager.getMatch().getParticipants().contains(killed)) {
                if (player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    damager.getMatch().onDeath(killer, killed);
                    killed.getPlayer().getInventory().clear();
                    player.setHealth(20);
                    VisualUtils.playDeathAnimation(player, (Player) entityEvent.getDamager());
                }

            }
            return;
        }
        if (event instanceof EntityDamageByBlockEvent) {
            if (user.getMatch() == null) {
                event.setCancelled(true);
                return;
            }
            Participant killed = user.getMatch().getParticipant(user);
            if (user.getMatch().getParticipants().contains(killed)) {
                if (player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    user.getMatch().onDeath(null, killed);
                    player.setHealth(20);
                }
            }
            return;
        }
        if (event.getFinalDamage() >= player.getHealth()) {
            User damaged = Practice.getInstance().getUserManager().getUser(((Player) event).getUniqueId());
            if (damaged.getStatus() != UserStatus.IN_MATCH) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.setHealth(20);
            damaged.getMatch().onDeath(null, damaged.getMatch().getParticipant(damaged));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killerPlayer = event.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }
        User dead = Practice.getInstance().getUserManager().getUser(deadPlayer.getUniqueId());
        User killer = Practice.getInstance().getUserManager().getUser(killerPlayer.getUniqueId());
        if (dead.getStatus() != UserStatus.IN_MATCH) {
            return;
        }
        Participant killerParticipant = killer.getMatch().getParticipant(killer);
        Participant deadParticipant = dead.getMatch().getParticipant(dead);
        if (dead.getMatch().getParticipants().contains(killerParticipant)) {
            dead.getMatch().onDeath(killerParticipant, deadParticipant);
        }

        deadPlayer.spigot().respawn();
        ConsoleUtils.debug(deadPlayer.getName() + " death");

        event.getDrops().clear();
    }
}