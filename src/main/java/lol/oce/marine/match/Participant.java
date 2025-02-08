package lol.oce.marine.match;

import lol.oce.marine.Practice;
import lol.oce.marine.match.impl.MatchSnapshot;
import lol.oce.marine.players.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class Participant {
    private final UUID uuid;
    private final boolean alive;
    private final Color color;
    private MatchSnapshot matchSnapshot;

    public Participant(UUID uuid, Color color) {
        this.uuid = uuid;
        this.alive = true;
        this.color = color;
        this.matchSnapshot = new MatchSnapshot(getPlayer());
    }

    public Participant(User user, Color color) {
        this.uuid = user.getUuid();
        this.alive = true;
        this.color = color;
    }

    public User getUser() {
        return Practice.getInstance().getUserManager().getUser(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
