package lol.oce.marine.match.modes;

import com.connorlinfoot.titleapi.TitleAPI;
import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.configs.ConfigService;
import lol.oce.marine.configs.impl.SettingsLocale;
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
    int hits;


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
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &b" + countdown[0] + " &fseconds!"));
                }
                if (SettingsLocale.DEBUG.getBoolean()) {
                    hits = 15;
                } else {
                    hits = 100;
                }
                if (countdown[0] == 0) {
                    setStarted(true);
                    startTime();
                    for (Participant participant : getParticipants()) {
                        if (getKit().isBoxing() && SettingsLocale.DEBUG.getBoolean()) {
                            participant.getPlayer().sendMessage(StringUtils.handle("&eWARNING: Since debug mode is enabled, the match will end after 15 hits."));
                        }
                    }
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
                    participant.getPlayer().sendMessage(" ");
                    participant.getPlayer().sendMessage(StringUtils.handle("&b&lMatch result:"));
                    participant.getPlayer().sendMessage(StringUtils.handle("&aWinner: &e" + won.getPlayer().getName() + " &7| &cLoser: &e" + getOpponent(won).getPlayer().getName()));
                    if (!getSpectators().isEmpty()) {
                        participant.getPlayer().sendMessage(StringUtils.handle("&7"));
                        participant.getPlayer().sendMessage(StringUtils.handle("&bSpectators:"));
                        participant.getPlayer().sendMessage(StringUtils.handle("&7" + getSpectators().toString()));
                    }
                    participant.getPlayer().sendMessage(" ");
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
            lines.add(StringUtils.line("&7", 15));
            lines.add(StringUtils.handle("&fOpponent: &b" + getOpponent(participant).getPlayer().getName()));
            lines.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(getTime())));
            lines.add(StringUtils.handle("&7"));
            if (getKit().isBoxing()) {
                lines.add(StringUtils.handle("&fHits: &b"));
                lines.add(StringUtils.handle("&f You: &b" + participant.getMatchSnapshot().getHits()
                        + " &7/ &f" + hits));
                lines.add(StringUtils.handle("&f Opponent: &b" + opponent.getMatchSnapshot().getHits() + " &7/ &f" + hits));
            }

            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fYour ping: &b" + ((CraftPlayer) participant.getPlayer()).getHandle().ping + " ms"));
            lines.add(StringUtils.handle("&fOpponent's ping: &b" + "0" + " ms"));
        } else {
            lines.add(StringUtils.handle("&7"));
            boolean isWinner = isWinner(participant);
            lines.add(StringUtils.handle(isWinner ? "&a&lVICTORY" : "&c&lDEFEAT"));
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(getTime())));
        }
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&bMade by Ocean"));
        lines.add(StringUtils.line("&7", 15));
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
        getParticipant(Practice.getInstance().getUserManager().getUser(event.getDamager().getUniqueId())).getMatchSnapshot().handleHits(this);
        if (getKit().isBoxing()) {
            Player damager = (Player) event.getDamager();
            event.setDamage(0);

            if (getParticipant(Practice.getInstance().getUserManager().getUser(event.getDamager().getUniqueId())).getMatchSnapshot().getHits() == hits) {
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
        getParticipant(Practice.getInstance().getUserManager().getUser(event.getEntity().getUniqueId())).getMatchSnapshot().handleRegen();
    }

    @Override
    public void onEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            getParticipant(Practice.getInstance().getUserManager().getUser(event.getPlayer().getUniqueId())).getMatchSnapshot().addGaps();
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
