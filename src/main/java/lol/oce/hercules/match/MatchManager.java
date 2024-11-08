package lol.oce.hercules.match;

import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.match.modes.OneVersusOneMatch;
import lol.oce.hercules.players.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MatchManager {

    List<Match> matches = new ArrayList<>();

    public void startSolo(MatchType type, Kit kit, User[] red, User[] blue, boolean ranked) {
        List<User> players = new ArrayList<>();
        players.addAll(Arrays.asList(red));
        players.addAll(Arrays.asList(blue));
        OneVersusOneMatch match = new OneVersusOneMatch(players.get(0), players.get(1), null, null, ranked, kit, type);
        matches.add(match);
        match.start();
        for (User user : players) {
            user.setMatch(match);
        }
    }

    // TODO: Team matches
//    public void startTeam(MatchType type, Kit kit, User[] red, User[] blue, boolean ranked) {
//        List<User> players = new ArrayList<>();
//        players.addAll(Arrays.asList(red));
//        players.addAll(Arrays.asList(blue));
//        TeamMatch match = new TeamMatch(players, null, ranked, kit, type);
//        match.start();
//        for (User user : players) {
//            user.setMatch(match);
//        }
//    }
}
