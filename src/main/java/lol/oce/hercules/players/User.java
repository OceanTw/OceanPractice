package lol.oce.hercules.players;

import lol.oce.hercules.match.Match;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class User {
    Player player;
    UserKitStats kitStats;
    UserStatus status;
    Match match;
}
