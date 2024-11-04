package lol.oce.hercules.match;

import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lombok.Builder;
import lombok.Data;

@Builder(setterPrefix = "set")
@Data
public class Queue {
    User user;
    boolean ranked;
    Kit kit;
    int queueRange;
}
