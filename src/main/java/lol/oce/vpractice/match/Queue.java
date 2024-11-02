package lol.oce.vpractice.match;

import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.players.User;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

@Builder(setterPrefix = "set")
@Data
public class Queue {
    User user;
    boolean ranked;
    Kit kit;
    int queueRange;
}
