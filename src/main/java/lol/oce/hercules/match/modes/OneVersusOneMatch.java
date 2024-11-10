package lol.oce.hercules.match.modes;

import com.connorlinfoot.titleapi.TitleAPI;
import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.match.Match;
import lol.oce.hercules.match.MatchType;
import lol.oce.hercules.match.Participant;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.ConsoleUtils;
import lol.oce.hercules.utils.StringUtils;
import lol.oce.hercules.utils.TimeUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
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

        ConsoleUtils.debug("Processing match start for " + red.getPlayer().getName() + " and " + blue.getPlayer().getName());

        final int[] countdown = {5};

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &c" + countdown[0] + " &fseconds!"));
                }
                if (countdown[0] == 0) {
                    setStarted(true);
                    startTime();
                    cancel();
                }
                countdown[0]--;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    @Override
    public void end(Participant won) {
        if (won == null) {
            for (Participant participant : getParticipants()) {
                participant.getPlayer().sendMessage(StringUtils.handle("&fMatch has been voided!"));
                Practice.getUserManager().resetUser(participant.getUser());
                return;
            }
        }
        setWinner(won);
        ConsoleUtils.debug(won.getPlayer().getName() + " won the match against " + getOpponent(won).getPlayer().getName());
        for (Participant participant : getParticipants()) {
            participant.getUser().setStatus(UserStatus.IN_POST_MATCH);
            if (participant.getColor().equals(won.getColor())) {
                TitleAPI.sendTitle(participant.getPlayer(), 20, 40, 20, "&a&lYOU WON", "&a" + won.getPlayer().getName() + " &fhas won the match!");
            } else {
                TitleAPI.sendTitle(participant.getPlayer(), 20, 40, 20, "&c&lYOU LOST", "&a" + won.getPlayer().getName() + " &fhas won the match!");
            }
        }
        stopTime();
        setEnded(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    // TODO: Post match inventory
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch has ended!"));
                    Practice.getUserManager().resetUser(participant.getUser());
                    Practice.getMatchManager().endMatch(OneVersusOneMatch.this);
                    participant.getPlayer().setAllowFlight(false);
                    participant.getPlayer().setFlying(false);
                }
                cancel();
            }
        }.runTaskLater(Practice.getPlugin(), 20 * 5);
    }

    @Override
    public void onDeath(Participant killer, Participant killed) {
        if (killer == null) {
            killer = getOpponent(killed);
        }
        for (Participant participant : getParticipants()) {
            participant.getPlayer().sendMessage(StringUtils.handle("&d" + killed.getPlayer().getName() + " &7was killed by &d" + killer.getPlayer().getName() + "!"));
        }
        killer.getPlayer().hidePlayer(killed.getPlayer());
        killed.getPlayer().setAllowFlight(true);
        killed.getPlayer().setFlying(true);
        ConsoleUtils.debug(killed.getPlayer().getName() + " died to " + killer.getPlayer().getName());
        end(killer);
    }

    @Override
    public List<String> getLines(Participant participant) {
        Participant opponent = getOpponent(participant);
        List<String> lines = new ArrayList<>();
        if (!isEnded()) {
            lines.add(StringUtils.handle("&fOpponent: &c" + getOpponent(participant).getPlayer().getName()));
            lines.add(StringUtils.handle("&fDuration: &c" + TimeUtils.formatTime(getTime())));
            if (getKit().isBoxing()) {
                lines.add(StringUtils.handle("&cHits: &c"));
                lines.add(StringUtils.handle("&f You: &c" + hits.getOrDefault(participant.getPlayer(), 0)
                        + " &7/ &f100"));
                lines.add(StringUtils.handle("&f Opponent: &c" + hits.getOrDefault(getOpponent(participant).getPlayer(), 0) + " &7/ &f100"));
            }

            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fYour ping: &c" + ((CraftPlayer) participant.getPlayer()).getHandle().ping) + " ms");
            lines.add(StringUtils.handle("&fTheir ping: &c" + ((CraftPlayer) opponent.getPlayer()).getHandle().ping) + " ms");
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&caether.rip"));
        } else {
            lines.add(StringUtils.handle("&7"));
            boolean isWinner = isWinner(participant);
            lines.add(StringUtils.handle(isWinner ? "&a&lVICTORY" : "&c&lDEFEAT"));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fDuration: &c" + TimeUtils.formatTime(getTime())));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&caether.rip"));
        }
        return lines;
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
        red.getPlayer().getInventory().setContents(getKit().getContent());
        blue.getPlayer().getInventory().setContents(getKit().getContent());
        red.getPlayer().getInventory().setArmorContents(getKit().getArmorContent());
        blue.getPlayer().getInventory().setArmorContents(getKit().getArmorContent());
        red.getPlayer().updateInventory();
        blue.getPlayer().updateInventory();

        for (PotionEffect effect : getKit().getPotionEffects()) {
            red.getPlayer().addPotionEffect(effect);
            blue.getPlayer().addPotionEffect(effect);
        }
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
            return;
        }
        if (getKit().isBoxing()) {
            Player damager = (Player) event.getDamager();
            hits.put((Player) event.getDamager(), hits.getOrDefault(damager, 0) + 1);
            damager.sendMessage(StringUtils.handle("&fHits: &c" + hits.get(damager)));
            event.setDamage(0);
            if (hits.get(damager) == 15) {
                Participant killer = getParticipant(Practice.getUserManager().getUser(damager.getUniqueId()));
                Participant killed = getParticipant(Practice.getUserManager().getUser(event.getEntity().getUniqueId()));
                onDeath(killer, killed);
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
        return user.equals(red.getUser()) ? red : blue;
    }

    private Participant getOpponent(Participant participant) {
        return participant.equals(red) ? blue : red;
    }

    private boolean isWinner(Participant participant) {
        return participant.equals(red) ? isEnded() && red.equals(getWinner()) : isEnded() && blue.equals(getWinner());
    }

}
