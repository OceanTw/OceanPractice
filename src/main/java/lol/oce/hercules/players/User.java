package lol.oce.hercules.players;

import lol.oce.hercules.match.Match;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@Builder(setterPrefix = "set")
public class User {
    Player player;
    UserKitStats kitStats;
    UserStatus status;
    Match match;
}
