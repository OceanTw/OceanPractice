package lol.oce.hercules.duels;

import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Request {
    User sender;
    User receiver;
    Kit kit;
}
