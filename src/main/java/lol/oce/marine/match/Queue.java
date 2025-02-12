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
    public User user;
    public boolean ranked;
    public Kit kit;
    public int queueRange;
    public int queueTime;
}
