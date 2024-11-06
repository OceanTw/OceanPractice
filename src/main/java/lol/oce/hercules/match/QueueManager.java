package lol.oce.hercules.match;

import lol.oce.hercules.Practice;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QueueManager {
    List<Queue> queues = new ArrayList<>();
    Map<Queue, BukkitTask> queueTimeMap = new HashMap<>();

    public void joinQueue(User user, Kit kit, boolean ranked) {
        Queue queue;
        if (ranked) {
            queue = Queue.builder()
                    .setKit(kit)
                    .setRanked(true)
                    .setUser(user)
                    .setQueueRange(100)
                    .build();
        } else {
            queue = Queue.builder()
                    .setKit(kit)
                    .setRanked(false)
                    .setUser(user)
                    .setQueueRange(0)
                    .build();
        }
        queues.add(queue);
        user.setQueue(queue);
        startQueueTimer(queue);
        queues.stream().forEach(q -> {
            if (q.kit == kit && q.ranked == ranked) {
                // Matchmaking logic
                if (ranked) {
                    // Ranked matchmaking logic
                    // the elo range is the user's elo - elo range and the user's elo + elo range;
                    int eloRange = queue.queueRange;
                    int userElo = 1000;
                    int minElo = userElo - eloRange;
                    int maxElo = userElo + eloRange;
                    if (1000 >= minElo && 1000 <= maxElo) {
                        // Match found
                    }
                } else {
                    // Match found
                }
            }
        });
    }

    public void leaveQueue(User user) {
    }

    public Queue getQueue(Player player) {
        return queues.stream().filter(q -> q.user.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public void startQueueTimer(Queue queue) {
        queue.setQueueTime(0);
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                queue.setQueueTime(queue.getQueueTime() + 1);
            }
        }.runTaskTimerAsynchronously(Practice.getPlugin(), 0, 20);
        queueTimeMap.put(queue, runnable);
    }

    public void stopQueueTimer(Queue queue) {
        queueTimeMap.get(queue).cancel();
        queueTimeMap.remove(queue);
    }
}
