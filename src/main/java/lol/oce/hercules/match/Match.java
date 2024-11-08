package lol.oce.hercules.match;

import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerHealthChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
    private boolean started = false;

    public void start() {
        if (getKit().isBuild()) getArena().takeChunkSnapshots();
        teleportAll();
        giveKits();

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