package lol.oce.hercules.players;

import lol.oce.hercules.match.Match;
import lol.oce.hercules.match.Queue;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@Builder(setterPrefix = "set")
public class User {
    UUID uuid;
//    UserKitStats kitStats;
    UserStatus status;
    Match match;
    Queue queue;

    public boolean isInMatch() {
        return status == UserStatus.IN_MATCH;
    }

    public boolean isInQueue() {
        return status == UserStatus.IN_QUEUE;
    }

    public boolean isInLobby() {
        return status == UserStatus.IN_LOBBY;
    }

    public boolean isEditingKit() {
        return status == UserStatus.IN_KIT_EDITOR;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
