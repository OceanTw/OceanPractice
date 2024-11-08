package lol.oce.hercules.adapters;

import lol.oce.hercules.Practice;
import lol.oce.hercules.match.Participant;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.utils.StringUtils;
import lol.oce.hercules.utils.TimeUtils;
import lol.oce.hercules.utils.scoreboards.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ScoreboardAdapter implements AssembleAdapter {

    private User getUser(Player player) {
        User user = Practice.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            Practice.getInstance().getLogger().log(Level.SEVERE, "Error loading user for player " + player.getName());
        }
        return user;
    }

    @Override
    public String getTitle(Player player) {
        return StringUtils.handle("&5&lPractice &7[NA]");
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
        lines.add(StringUtils.handle("&fOnline: &5" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&fPlaying: &5" + Practice.getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&fIn Queue: &5" + Practice.getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&5aether.rip"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }

    private List<String> getInQueueLines(User user) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 15));
        lines.add(StringUtils.handle("&fOnline: &5" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&fPlaying: &5" + Practice.getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&fIn Queue: &5" + Practice.getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&5&lQueue"));
        lines.add(StringUtils.handle("&f  Kit: &5" + user.getQueue().getKit().getDisplayName()));
        lines.add(StringUtils.handle("&f  Type: &5" + (user.getQueue().isRanked() ? "Ranked" : "Unranked")));
        lines.add(StringUtils.handle("&f  Duration: &5" + TimeUtils.formatTime(user.getQueue().getQueueTime())));
        lines.add(StringUtils.handle("&5aether.rip"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }

    private List<String> getMatchLines(User user) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 15));
        Participant self = user.getMatch().getParticipant(user);
        Participant opponent = null;
        for (Participant participant : user.getMatch().getParticipants()) {
            if (!participant.getUuid().equals(user.getUuid())) {
                opponent = participant;
                break;
            }
        }
        if (opponent == null) {
            lines.add("Error loading scoreboard");
            lines.add("Opponent is null");
            lines.add("Please contact an admin");
            return lines;
        }
        lines.add(StringUtils.handle("Opponent: &5" + opponent.getPlayer().getName()));
        lines.add(StringUtils.handle("&fDuration: &5" + TimeUtils.formatTime(user.getMatch().getTime())));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&5aether.rip"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }
}