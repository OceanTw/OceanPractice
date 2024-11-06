package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.arenas.ArenaType;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.players.UserStatus;
import lol.oce.hercules.utils.StringUtils;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.UUID;

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
        if (arena.getType() == ArenaType.STANDALONE) {
            arena.takeChunkSnapshots();
        }

        final int[] countdown = {5};
        for (User user : players) {
            user.setStatus(UserStatus.IN_MATCH);
        }
        red.forEach(user -> user.getPlayer().teleport(arena.getRedSpawn()));
        blue.forEach(user -> user.getPlayer().teleport(arena.getBlueSpawn()));
        giveKit();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                for (User user : players) {
                    user.getPlayer().sendMessage(StringUtils.handle("&fMatch starting in &5" + countdown[0] + " &fseconds!"));
                }
                if (countdown[0] == 0) {
                    started = true;
                    cancel();
                }
                countdown[0]--;
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    public void end(User winner) {
        List<User> winningTeam = red.contains(UserManager.getUser(UUID.fromString(winner.getUuid().toString()))) ? red : blue;
        List<User> losingTeam = red.contains(UserManager.getUser(UUID.fromString(winner.getUuid().toString()))) ? blue : red;
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

    public void playerKilled(User killer, User killed) {
        if (red.contains(killed)) {
            red.remove(killed);
        } else {
            blue.remove(killed);
        }
        spectators.add(killed);
        killed.getPlayer().sendMessage(StringUtils.handle("&cYou have been eliminated!"));
        // TODO: Add coins to the killer
        if (red.isEmpty()) {
            end(blue.get(0));
        } else if (blue.isEmpty()) {
            end(red.get(0));
        }
    }

    public void forfeit(User ff) {
        List<User> winningTeam = red.contains(UserManager.getUser(UUID.fromString(ff.getUuid().toString()))) ? blue : red;
        end(winningTeam.get(0));
    }

    public void giveKit() {
        for (User user : players) {
            user.getPlayer().getInventory().clear();
            user.getPlayer().getInventory().setArmorContents(null);
            Inventory kitinv = getKit().getInventory();
            user.getPlayer().getInventory().setContents(kit.getInventory().getContents());
            user.getPlayer().getInventory().setHelmet(kit.getHelmet());
            user.getPlayer().getInventory().setChestplate(kit.getChestplate());
            user.getPlayer().getInventory().setLeggings(kit.getLeggings());
            user.getPlayer().getInventory().setBoots(kit.getBoots());
        }
    }
}
