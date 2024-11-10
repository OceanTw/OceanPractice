package rip.venus.star.match;

import rip.venus.star.Practice;
import rip.venus.star.players.User;
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

    public Participant(UUID uuid, Color color) {
        this.uuid = uuid;
        this.alive = true;
        this.color = color;
    }

    public Participant(User user, Color color) {
        this.uuid = user.getUuid();
        this.alive = true;
        this.color = color;
    }

    public User getUser() {
        return Practice.getUserManager().getUser(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
