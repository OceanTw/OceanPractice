package lol.oce.marine.duels;

import lol.oce.marine.kits.Kit;
import lol.oce.marine.players.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Request {
    User sender;
    User receiver;
    Kit kit;
}
