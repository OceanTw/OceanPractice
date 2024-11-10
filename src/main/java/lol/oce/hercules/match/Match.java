package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserStatus;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Time;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class Match {
    private final Arena arena;
    private final List<User> spectators;
    private final boolean ranked;
    private final Kit kit;
    private final MatchType type;
    private int time = 0;
    private boolean started = false;
    private boolean ended = false;
    private Participant winner = null;
    private BukkitTask runnable;

    public void start() {
        if (getKit().isBuild()) getArena().takeChunkSnapshots();
        teleportAll();
        giveKits();
        forEachParticipant(participant -> participant.getUser().setStatus(UserStatus.IN_MATCH));

        onStart();
    }

    public void forEachParticipant(Consumer<Participant> action) {
        for (Participant participant : getParticipants()) {
            Player player = participant.getPlayer();
            if (player != null) {
                action.accept(participant);
            }
        }
    }

    public void startTime() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                time++;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    public void stopTime() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    public abstract List<Participant> getParticipants();

    public abstract Participant getParticipant(User user);

    public abstract void onStart();

    public abstract void end(Participant winner);

    public abstract void onDeath(Participant killer, Participant killed);

    public abstract List<String> getLines(Participant participant);

    public abstract void forfeit(Participant participant);

    public abstract void voidMatch();

    public abstract void teleportAll();

    public abstract void giveKits();

    public abstract void onMove(PlayerMoveEvent event);

    public abstract void onHit(EntityDamageByEntityEvent event);

    public abstract void onBlockPlace(BlockPlaceEvent event);

    public abstract void onBlockBreak(BlockBreakEvent event);

    public abstract void onRegen(PlayerHealthChangeEvent event);

    public abstract void onEat(PlayerItemConsumeEvent event);
}