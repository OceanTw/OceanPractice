package lol.oce.vpractice.duels;

import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.players.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Request {
    User sender;
    User receiver;
    Kit kit;
}
