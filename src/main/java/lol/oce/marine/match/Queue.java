package lol.oce.marine.match;

import lol.oce.marine.kits.Kit;
import lol.oce.marine.players.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(setterPrefix = "set")
@Getter
@Setter
public class Queue {
    User user;
    boolean ranked;
    Kit kit;
    int queueRange;
    int queueTime;
}
