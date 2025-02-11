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

public class TeamVersusTeamMatch extends Match {

    private final List<Participant> red;
    private final List<Participant> blue;
    int redHits;
    int blueHits;


    public TeamVersusTeamMatch(List<User> team1, List<User> team2, Arena arena, List<User> spectators, boolean ranked, Kit kit, MatchType type) {
        super(arena, spectators, ranked, kit, type);

        red = team1.stream().map(user -> new Participant(user, Color.RED)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        blue = team2.stream().map(user -> new Participant(user, Color.BLUE)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        participants.addAll(red);
        participants.addAll(blue);
        return participants;
    }

    @Override
    public void onStart() {

        final int[] countdown = {5};

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Participant participant : getParticipants()) {
                    participant.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &b" + countdown[0] + " &fseconds!"));
                }
                if (SettingsLocale.DEBUG.getBoolean()) {
                    redHits = 15 * red.size();
                    blueHits = 15 * blue.size();
                } else {
                    redHits = 100 * red.size();
                    blueHits = 100 * blue.size();
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
                    String winner = won.getColor().equals(Color.RED) ? "&cRed" : "&bBlue";
                    String loser = won.getColor().equals(Color.RED) ? "&bBlue" : "&cRed";
                    participant.getPlayer().sendMessage(StringUtils.handle("&aWinner: &e" + winner + " &7| &cLoser: &e" + loser));
                    if (!getSpectators().isEmpty()) {
                        participant.getPlayer().sendMessage(StringUtils.handle("&7"));
                        participant.getPlayer().sendMessage(StringUtils.handle("&bSpectators:"));
                        participant.getPlayer().sendMessage(StringUtils.handle("&7" + getSpectators().toString()));
                    }
                    participant.getPlayer().sendMessage(" ");
                    Practice.getInstance().getUserManager().resetUser(participant.getUser());
                    Practice.getInstance().getMatchManager().endMatch(TeamVersusTeamMatch.this);
                    participant.getPlayer().setAllowFlight(false);
                    participant.getPlayer().setFlying(false);
                }
                cancel();
            }
        }.runTaskLater(Practice.getInstance(), 20 * 5);
    }

    @Override
    public void onDeath(Participant killer, Participant killed) {
        getParticipants().remove(killed);
        for (Participant participant : getParticipants()) {
            participant.getPlayer().sendMessage(StringUtils.handle("&b" + killed.getPlayer().getName() + " &7was killed by &b" + killer.getPlayer().getName() + "!"));
            participant.getPlayer().hidePlayer(killed.getPlayer());
        }
        getSpectators().add(killed.getUser());
        killed.getPlayer().setAllowFlight(true);
        killed.getPlayer().setFlying(true);
        ConsoleUtils.debug(killed.getPlayer().getName() + " died to " + killer.getPlayer().getName());
        if (killed.getColor().equals(Color.RED)) {
            red.remove(killed);
        } else {
            blue.remove(killed);
        }
        if (red.isEmpty() || blue.isEmpty()) {
            end(red.isEmpty() ? blue.get(0) : red.get(0));
        }
    }

    @Override
    public List<String> getLines(Participant participant) {
        List<Participant> team = participant.getColor().equals(Color.RED) ? red : blue;
        List<Participant> opponent = getOpponent(participant);
        List<String> lines = new ArrayList<>();
        if (!isEnded()) {
            lines.add(StringUtils.line("&7", 15));
            lines.add(StringUtils.handle("&fOpponents left: &b" + getOpponent(participant).size()));
            lines.add(StringUtils.handle("&fDuration: &b" + TimeUtils.formatTime(getTime())));
            lines.add(StringUtils.handle("&7"));
            int requiredHits = participant.getColor().equals(Color.RED) ? redHits : blueHits;
            int opponentRequiredHits = participant.getColor().equals(Color.RED) ? blueHits : redHits;
            int hits = 0;
            for (Participant p : team) {
                hits += p.getMatchSnapshot().getHits();
            }
            int opponentHits = 0;
            for (Participant p : opponent) {
                opponentHits += p.getMatchSnapshot().getHits();
            }
            if (getKit().isBoxing()) {
                lines.add(StringUtils.handle("&fHits: &b"));
                lines.add(StringUtils.handle("&f You: &b" + hits
                        + " &7/ &f" + requiredHits));
                lines.add(StringUtils.handle("&f Opponent: &b" + opponentHits + " &7/ &f" + opponentRequiredHits));
            }
            lines.add(StringUtils.handle("&7"));
            lines.add(StringUtils.handle("&fYour ping: &b" + "0" + " ms"));
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
        // remove the player from the match
        for (Participant p : getParticipants()) {
            p.getPlayer().sendMessage(StringUtils.handle("&d" + participant.getPlayer().getName() + " &7has abandoned the match!"));
        }
        if (participant.getColor().equals(Color.RED)) {
            red.remove(participant);
        } else {
            blue.remove(participant);
        }
    }

    @Override
    public void voidMatch() {
        end(null);
    }

    @Override
    public void teleportAll() {
        red.forEach(participant -> participant.getPlayer().teleport(getArena().getRedSpawn()));
        blue.forEach(participant -> participant.getPlayer().teleport(getArena().getBlueSpawn()));
    }

    @Override
    public void giveKits() {
        for (Participant participant : getParticipants()) {
            Player player = participant.getPlayer();
            player.getInventory().setContents(getKit().getContent());
            player.getInventory().setArmorContents(getKit().getArmorContent());
            player.updateInventory();

            for (PotionEffect effect : getKit().getPotionEffects()) {
                player.addPotionEffect(effect);
            }
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
        getParticipant(Practice.getInstance().getUserManager().getUser(event.getEntity().getUniqueId())).getMatchSnapshot().handleHits(this);
        if (getKit().isBoxing()) {
            Player damager = (Player) event.getDamager();
            event.setDamage(0);

            if (getParticipant(Practice.getInstance().getUserManager().getUser(event.getEntity().getUniqueId())).getMatchSnapshot().getHits() == (getParticipant(Practice.getInstance().getUserManager().getUser(event.getEntity().getUniqueId())).getColor().equals(Color.RED) ? redHits : blueHits)) {
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
        // TODO: Implement this check
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

    private List<Participant> getOpponent(Participant participant) {
        return participant.getColor().equals(Color.RED) ? blue : red;
    }

    private boolean isWinner(Participant participant) {
        return participant.equals(red) ? isEnded() && red.equals(getWinner()) : isEnded() && blue.equals(getWinner());
    }

}
