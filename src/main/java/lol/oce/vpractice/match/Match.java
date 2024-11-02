package lol.oce.vpractice.match;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.players.User;
import lol.oce.vpractice.players.UserStatus;
import lol.oce.vpractice.utils.StringUtils;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@Builder(setterPrefix = "set")
@Data
public class Match {
    private List<User> red;
    private List<User> blue;
    private List<User> players;
    private List<User> spectators;
    private boolean ranked;
    private Kit kit;
    private boolean started;

    public void start() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        final int[] countdown = {5};
        for (User user : players) {
            user.setStatus(UserStatus.IN_MATCH);
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (User user : players) {
                    user.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &9" + countdown[0] + " &fseconds!"));
                }
                if (countdown[0] == 0) {
                    started = true;
                    cancel();
                }
                countdown[0]--;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    public void end(Player winner) {
        List<User> winningTeam = red.contains(Practice.getUserManager().getUser(winner.getUniqueId())) ? red : blue;
        List<User> losingTeam = red.contains(Practice.getUserManager().getUser(winner.getUniqueId())) ? blue : red;
        for (User user : winningTeam) {
            user.getPlayer().sendTitle(StringUtils.handle("&a&lYOU WON!"), "");
        }
        for (User user : losingTeam) {
            user.getPlayer().sendTitle(StringUtils.handle("&c&lYOU LOST!"), "");
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (User user : players) {
                    user.setStatus(UserStatus.IN_LOBBY);
                }
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20 * 5);
        for (User user : players) {
            Practice.getUserManager().resetUser(user);
        }
    }
}
