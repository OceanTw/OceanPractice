package lol.oce.hercules.listeners;

import lol.oce.hercules.Practice;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.VisualUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import static sun.audio.AudioPlayer.player;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Practice.getLobbyManager().giveItems(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        // if the server is not loaded, don't load the user
        if (!Practice.getInstance().isEnabled()) {
            return;
        }
        Practice.getUserManager().load(event.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent event) {
        // if the server is not loaded, don't load the user
        Practice.getUserManager().unload(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        User user = UserManager.getUser(player.getUniqueId());
        User damager;
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (!(entityEvent.getDamager() instanceof Player)) {
                return;
            }
            damager = UserManager.getUser(((Player) entityEvent.getDamager()).getUniqueId());
            if (damager.getMatch() == null || user.getMatch() == null) {
                return;
            }
            if (damager.getMatch().getPlayers().contains(user)) {
                if (player.getHealth() - event.getFinalDamage() <= 0) {
                    event.setCancelled(true);
                    damager.getMatch().playerKilled(user, damager);
                    player.setHealth(20);
                    VisualUtils.playDeathAnimation(player, (Player) entityEvent.getDamager());

                    if (user.getMatch().getPlayers().size() >=  3) {
                        // drop the items from the player
                        for (ItemStack item : player.getInventory().getContents()) {
                            if (item == null) {
                                continue;
                            }
                            player.getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }

                }
            }

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killerPlayer = event.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }
        User dead = UserManager.getUser(deadPlayer.getUniqueId());
        User killer = UserManager.getUser(killerPlayer.getUniqueId());
        if (dead.getMatch() == null || killer.getMatch() == null) {
            return;
        }
        if (dead.getMatch().getPlayers().contains(killer)) {
            dead.getMatch().playerKilled(killer, dead);
        }

        deadPlayer.spigot().respawn();

        if (dead.getMatch().getPlayers().size() >=  3) {
            return;
        }

        event.getDrops().clear();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        if (user.getStatus() == UserStatus.IN_MATCH) {
            if (!user.getMatch().isStarted()) {
                event.setCancelled(true);
            }
        }
    }
}
