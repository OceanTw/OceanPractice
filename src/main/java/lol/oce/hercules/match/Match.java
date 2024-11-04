package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.StringUtils;
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
    Arena arena;
    List<User> red;
    List<User> blue;
    List<User> players;
    List<User> spectators;
    boolean ranked;
    Kit kit;
    boolean started;
    MatchType type;

    public void start() {
        arena.takeChunkSnapshots();

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        final int[] countdown = {5};
        for (User user : players) {
            user.setStatus(UserStatus.IN_MATCH);
        }
        red.forEach(user -> user.getPlayer().teleport(arena.getRedSpawn()));
        blue.forEach(user -> user.getPlayer().teleport(arena.getBlueSpawn()));
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
        arena.restore();
    }
}
