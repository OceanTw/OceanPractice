package rip.venus.star.match;

import rip.venus.star.arenas.Arena;
import rip.venus.star.kits.Kit;
import rip.venus.star.match.modes.OneVersusOneMatch;
import rip.venus.star.players.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MatchManager {

    List<Match> matches = new ArrayList<>();

    public void startSolo(Arena arena, MatchType type, Kit kit, User[] red, User[] blue, boolean ranked) {
        List<User> players = new ArrayList<>();
        players.addAll(Arrays.asList(red));
        players.addAll(Arrays.asList(blue));
        OneVersusOneMatch match = new OneVersusOneMatch(players.get(0), players.get(1), arena, null, ranked, kit, type);
        matches.add(match);
        match.start();
        for (User user : players) {
            user.setMatch(match);
        }
    }

    public void endMatch(Match match) {
        matches.remove(match);
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
