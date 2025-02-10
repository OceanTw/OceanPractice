package lol.oce.marine.adapters;

import lol.oce.marine.Practice;
import lol.oce.marine.players.User;
import lol.oce.marine.utils.StringUtils;
import lol.oce.marine.utils.TimeUtils;
import lol.oce.marine.utils.scoreboards.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ScoreboardAdapter implements AssembleAdapter {

    private User getUser(Player player) {
        User user = Practice.getInstance().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            player.kickPlayer("Error loading user data, please rejoin the server");
        }
        return user;
    }

    @Override
    public String getTitle(Player player) {
        return StringUtils.handle("&b&lPractice &7[NA]");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        User user = getUser(player);
        if (user == null) {
            lines.add("Error loading scoreboard");
            lines.add("User is null");
            lines.add("If rejoining the server");
            lines.add("doesn't fix this, please");
            lines.add("contact an admin");
            return lines;
        }
        switch (user.getStatus()) {
            case IN_LOBBY:
                lines = getLobbyLines(user);
                break;
            case IN_QUEUE:
                lines = getInQueueLines(user);
                break;
            case IN_MATCH:
            case IN_POST_MATCH:
                lines = getMatchLines(user);
                break;
            default:
                lines.add("Error loading scoreboard");
                lines.add("Invalid user status");
                lines.add("Please contact an admin");
                break;
        }
        return lines;
    }

    private List<String> getLobbyLines(User user) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 15));
        lines.add(StringUtils.handle("&fOnline: &b" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&fPlaying: &b" + Practice.getInstance().getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&fIn Queue: &b" + Practice.getInstance().getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&bMade by Ocean"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }

    private List<String> getInQueueLines(User user) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 15));
        lines.add(StringUtils.handle("&fOnline: &b" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&fPlaying: &b" + Practice.getInstance().getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&fIn Queue: &b" + Practice.getInstance().getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&b&lQueue"));
        lines.add(StringUtils.handle("&f  Kit: &b" + user.getQueue().getKit().getDisplayName()));
        lines.add(StringUtils.handle("&f  Type: &b" + (user.getQueue().isRanked() ? "Ranked" : "Unranked")));
        lines.add(StringUtils.handle("&f  Duration: &b" + TimeUtils.formatTime(user.getQueue().getQueueTime())));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&bMade by Ocean"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }

    private List<String> getMatchLines(User user) {
        return user.getMatch().getLines(user.getMatch().getParticipant(user));
    }
}