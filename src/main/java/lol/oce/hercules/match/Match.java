package lol.oce.hercules.match;

import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

@Builder(setterPrefix = "set")
@Data
public abstract class Match {
    private final Arena arena;
    private final List<User> spectators;
    private final boolean ranked;
    private final Kit kit;
    private final MatchType type;

    public void start() {
        if (getKit().isBuild()) getArena().takeChunkSnapshots();
        checkRules();
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

    public abstract void onStart();

    public abstract void end(Participant winner);

    public abstract void onDeath(Participant killer, Participant killed);

    public abstract List<String> getLines(Participant participant);

    public abstract void forfeit(Participant participant);

    public abstract void voidMatch();

    public abstract void teleportAll();

    public abstract void giveKits();

    public abstract void checkRules();
}