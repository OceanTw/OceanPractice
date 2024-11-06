package lol.oce.hercules.adapters;

import lol.oce.hercules.Practice;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.StringUtils;
import lol.oce.hercules.utils.TimeUtils;
import lol.oce.hercules.utils.scoreboards.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ScoreboardAdapter implements AssembleAdapter {

    private User getUser(Player player) {
        User user = UserManager.getUser(player.getUniqueId());
        if (user == null) {
            Practice.getInstance().getLogger().log(Level.SEVERE, "Error loading user for player " + player.getName());
        }
        return user;
    }

    @Override
    public String getTitle(Player player) {
        return StringUtils.handle("&b&lAether &f&lPractice");
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
//            case IN_MATCH:
//                lines = getMatchLines(user);
//                break;
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
        lines.add(StringUtils.handle("&b&lServer"));
        lines.add(StringUtils.handle("&f  Online: &b" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&f  Playing: &b" + Practice.getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&f  In Queue: &b" + Practice.getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&baether.rip"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }

    private List<String> getInQueueLines(User user) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 15));
        lines.add(StringUtils.handle("&b&lServer"));
        lines.add(StringUtils.handle("&f  Online: &b" + user.getPlayer().getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&f  Playing: &b" + Practice.getMatchManager().getMatches().size() * 2));
        lines.add(StringUtils.handle("&f  In Queue: &b" + Practice.getQueueManager().getQueues().size()));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&b&lQueue"));
        lines.add(StringUtils.handle("&f  Kit: &b" + user.getQueue().getKit().getDisplayName()));
        lines.add(StringUtils.handle("&f  Type: &b" + (user.getQueue().isRanked() ? "Ranked" : "Unranked")));
        lines.add(StringUtils.handle("&f  Duration: &b" + TimeUtils.formatTime(user.getQueue().getQueueTime())));
        lines.add(StringUtils.handle("&baether.rip"));
        lines.add(StringUtils.line("&7", 15));
        return lines;
    }
}