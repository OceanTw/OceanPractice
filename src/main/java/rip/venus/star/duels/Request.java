package rip.venus.star.duels;

import rip.venus.star.kits.Kit;
import rip.venus.star.players.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Request {
    User sender;
    User receiver;
    Kit kit;
}
