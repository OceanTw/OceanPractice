package lol.oce.vpractice.players;

import lol.oce.vpractice.match.Match;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class User {
    Player player;
    UserKitStats kitStats;
    UserStatus status;
    Match match;
}
