package lol.oce.marine.players;

import lombok.Getter;

@Getter
public enum UserStatus {
    IN_LOBBY("In Spawn"),
    IN_QUEUE("In Queue"),
    IN_MATCH("In Match"),
    IN_KIT_EDITOR("Editing Kit"),
    IN_POST_MATCH("Match Ending"),
    SPECTATING("Spectating match"),
    IN_PARTY("In Spawn (Party)"),
    OFFLINE("Offline");

    final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

}
