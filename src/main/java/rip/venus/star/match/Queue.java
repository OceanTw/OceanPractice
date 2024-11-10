package rip.venus.star.match;

import rip.venus.star.kits.Kit;
import rip.venus.star.players.User;
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
