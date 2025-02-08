package lol.oce.marine.match.modes;

import com.connorlinfoot.titleapi.TitleAPI;
import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.match.Match;
import lol.oce.marine.match.MatchType;
import lol.oce.marine.match.Participant;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserStatus;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.StringUtils;
import lol.oce.marine.utils.TimeUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class OneVersusOneMatch extends Match {

    private final Participant red;
    private final Participant blue;
    private final HashMap<UUID, Integer> hits;
    private final HashMap<UUID, Integer> gapsEaten;
    private final HashMap<UUID, Double> healthRegen;


    public OneVersusOneMatch(User player1, User player2, Arena arena, List<User> spectators, boolean ranked, Kit kit, MatchType type) {
        super(arena, spectators, ranked, kit, type);

        this.red = new Participant(player1, Color.RED);
        this.blue = new Participant(player2, Color.BLUE);
        this.hits = new HashMap<>();
        this.gapsEaten = new HashMap<>();
        this.healthRegen = new HashMap<>();
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
                Practice.getInstance().getUserManager().resetUser(participant.getUser());
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
                    participant.getPlayer().sendMessage(StringUtils.handle("&7 "));
                    participant.getPlayer().sendMessage(StringUtils.handle("&b&lMatch result:"));
                    participant.getPlayer().sendMessage(StringUtils.handle("&aWinner: &e" + won.getPlayer().getName() + " &7| &bLoser: &e" + getOpponent(won).getPlayer().getName()));
                    if (!getSpectators().isEmpty()) {
                        for (User spectator : getSpectators()) {
                            participant.getPlayer().sendMessage(StringUtils.handle("&7Spectator: &e" + spectator.getPlayer().getName()));
                        }
                    }
                    participant.getPlayer().sendMessage(StringUtils.handle("&7 "));
                    Practice.getInstance().getUserManager().resetUser(participant.getUser());
                    Practice.getInstance().getMatchManager().endMatch(OneVersusOneMatch.this);
                    participant.getPlayer().setAllowFlight(false);
                    participant.getPlayer().setFlying(false);
                }
                cancel();
            }
        }.runTaskLater(Practice.getInstance(), 20 * 5);
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
            lines.add(StringUtils.handle("&fOpponent: &b" + getOpponent(participant).getPlayer().getName()));
            lines.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(getTime())));
            if (getKit().isBoxing()) {
                lines.add(StringUtils.handle("&bHits: &b"));
                lines.add(StringUtils.handle("&f You: &b" + hits.getOrDefault(participant.getPlayer().getUniqueId(), 0)
                        + " &7/ &f100"));
                lines.add(StringUtils.handle("&f Opponent: &b" + hits.getOrDefault(getOpponent(participant).getPlayer().getUniqueId(), 0) + " &7/ &f100"));
            }

            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fYour ping: &b" + ((CraftPlayer) participant.getPlayer()).getHandle().ping + " ms"));
            lines.add(StringUtils.handle("&fOpponent's ping: &b" + ((CraftPlayer) opponent.getPlayer()).getHandle().ping + " ms"));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&bMade by Ocean"));
        } else {
            lines.add(StringUtils.handle("&7"));
            boolean isWinner = isWinner(participant);
            lines.add(StringUtils.handle(isWinner ? "&a&lVICTORY" : "&c&lDEFEAT"));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(getTime())));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&bMade by Ocean"));
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
        getParticipant((User) event.getEntity()).getMatchSnapshot().handleHits(this);
        if (!isStarted()) {
            event.setCancelled(true);
            return;
        }
        if (getKit().isBoxing()) {
            Player damager = (Player) event.getDamager();
            hits.put(event.getDamager().getUniqueId(), hits.getOrDefault(damager.getUniqueId(), 0) + 1);
            damager.sendMessage(StringUtils.handle("&fHits: &b" + hits.get(damager.getUniqueId())));
            event.setDamage(0);
            if (hits.get(damager.getUniqueId()) == 15) {
                Participant killer = getParticipant(Practice.getInstance().getUserManager().getUser(damager.getUniqueId()));
                Participant killed = getParticipant(Practice.getInstance().getUserManager().getUser(event.getEntity().getUniqueId()));
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
    public void onRegen(EntityRegainHealthEvent event) {
        healthRegen.put(event.getEntity().getUniqueId(), healthRegen.getOrDefault(event.getEntity().getUniqueId(), 0.0) + event.getAmount());
    }

    @Override
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            gapsEaten.put(event.getPlayer().getUniqueId(), gapsEaten.getOrDefault(event.getPlayer().getUniqueId(), 0) + 1);
        }
    }

    @Override
    public void onPotThrow(ProjectileLaunchEvent event) {
        Player player = (Player) event.getEntity().getShooter();
        getParticipant(player).getMatchSnapshot().addPots();
    }

    private Participant getOpponent(Participant participant) {
        return participant.equals(red) ? blue : red;
    }

    private boolean isWinner(Participant participant) {
        return participant.equals(red) ? isEnded() && red.equals(getWinner()) : isEnded() && blue.equals(getWinner());
    }

}
