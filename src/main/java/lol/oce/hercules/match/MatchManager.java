package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchManager {

    List<Match> matches = new ArrayList<>();

    public void startMatch(MatchType type, Kit kit, User[] red, User[] blue, boolean ranked) {
        List<User> players = new ArrayList<>();
        players.addAll(Arrays.asList(red));
        players.addAll(Arrays.asList(blue));
        Match match = Match.builder()
            .setArena(Practice.getArenaManager().getEnabledArenas().get(0))
            .setRed(Arrays.asList(red))
            .setBlue(Arrays.asList(blue))
            .setPlayers(players)
            .setSpectators(new ArrayList<>())
            .setRanked(ranked)
            .setKit(kit)
            .setStarted(false)
            .setType(type)
            .build();
        match.start();
        for (User user : players) {
            user.setMatch(match);
        }
    }
}
