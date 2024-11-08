package lol.oce.hercules.match.modes;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.match.Match;
import lol.oce.hercules.match.MatchType;
import lol.oce.hercules.match.Participant;
import lol.oce.hercules.players.User;
import lol.oce.hercules.utils.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class OneVersusOneMatch extends Match {

    private final Participant red;
    private final Participant blue;
    private final HashMap<Player, Integer> hits = new HashMap<>();
    private final HashMap<Player, Integer> blockedHits = new HashMap<>();
    private final HashMap<Player, Double> damage = new HashMap<>();
    private final HashMap<Player, Integer> gapsEaten = new HashMap<>();
    private final HashMap<Player, Double> healthRegen = new HashMap<>();

    public OneVersusOneMatch(User player1, User player2, Arena arena, List<User> spectators, boolean ranked, Kit kit, MatchType type) {
        super(arena, spectators, ranked, kit, type);

        this.red = new Participant(player1, Color.RED);
        this.blue = new Participant(player2, Color.BLUE);
    }

    @Override
    public List<Participant> getParticipants() {
        return Arrays.asList(red, blue);
    }

    @Override
    public void onStart() {

        final int[] countdown = {5};

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &5" + countdown[0] + " &fseconds!"));
                }
                if (countdown[0] == 0) {
                    setStarted(true);
                    cancel();
                }
                countdown[0]--;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    @Override
    public void end(Participant winner) {
        if (winner == null) {
            for (Participant participant : getParticipants()) {
                participant.getPlayer().sendMessage(StringUtils.handle("&fMatch has been voided!"));
                Practice.getUserManager().resetUser(participant.getUser());
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    // TODO: Post match inventory
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch has ended!"));
                    Practice.getUserManager().resetUser(participant.getUser());
                }
                cancel();
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20 * 5);
    }

    @Override
    public void onDeath(Participant killer, Participant killed) {
        if (killer == null) {
            killer = killed.equals(red) ? blue : red;
            for (Participant participant : getParticipants()) {
                participant.getPlayer().sendMessage(StringUtils.handle("&f" + killed.getPlayer().getName() + " &7was killed by" + killer.getPlayer().getName() + "!"));
            }
        }
        end(killer);
    }

    @Override
    public List<String> getLines(Participant participant) {
        return Collections.emptyList();
    }

    @Override
    public void forfeit(Participant participant) {
        end(participant.equals(red) ? blue : red);
    }

    @Override
    public void voidMatch() {
        end(null);
    }

    @Override
    public void teleportAll() {
        red.getPlayer().teleport(getArena().getRedSpawn());
        blue.getPlayer().teleport(getArena().getBlueSpawn());
    }

    @Override
    public void giveKits() {
        red.getPlayer().getInventory().setContents(getKit().getContents());
        blue.getPlayer().getInventory().setContents(getKit().getContents());

        red.getPlayer().getInventory().setContents(getKit().getContents());
        red.getPlayer().getInventory().setArmorContents(getKit().getArmor());
    }


    @Override
    public void onMove(PlayerMoveEvent event) {
        if (isStarted()) {
            return;
        }
        if (getKit().isFreezeOnStart()) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onHit(EntityDamageByEntityEvent event) {
        if (!isStarted()) {
            event.setCancelled(true);
        }
        if (getKit().isBoxing()) {
            Player damager = (Player) event.getDamager();
            hits.put((Player) event.getDamager(), hits.getOrDefault(damager, 0) + 1);
            event.setDamage(0);
            if (hits.get(damager) >= 100) {
                end(getParticipant(Practice.getUserManager().getUser(damager.getUniqueId())));
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if (getKit().isBuild()) {
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onRegen(PlayerHealthChangeEvent event) {
        double changes = event.getNewHealth() - event.getPreviousHealth();
        healthRegen.put(event.getPlayer(), healthRegen.getOrDefault(event.getPlayer(), 0.0) + changes);
    }

    @Override
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            gapsEaten.put(event.getPlayer(), gapsEaten.getOrDefault(event.getPlayer(), 0) + 1);
        }
    }

    @Override
    public Participant getParticipant(User user) {
        return user.equals(red.getPlayer()) ? red : blue;
    }


}
