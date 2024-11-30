package lol.oce.marine.match;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.players.User;
import lol.oce.marine.players.UserStatus;
import lol.oce.marine.utils.StringUtils;
import lombok.Getter;
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
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getPlayer().sendMessage(StringUtils.handle("&cYou are already in a match!"));
            return;
        }

        if (user.getQueue() != null) {
            user.getPlayer().sendMessage(StringUtils.handle("&cYou are already in a queue!"));
            return;
        }

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

        if (queues.size() == 1) {
            return;
        }

        for (Queue q : queues) {
            if (q.getKit() == kit && q.isRanked() == ranked) {
                if (q.getUser().getPlayer().getUniqueId() == user.getPlayer().getUniqueId()) {
                    continue;
                }

                stopQueueTimer(q);
                stopQueueTimer(queue);
                queues.remove(q);
                queues.remove(queue);
                user.setQueue(null);
                q.getUser().setQueue(null);

                Arena arena = Practice.getArenaManager().getRandomArena(kit);
                if (arena == null) {
                    user.getPlayer().sendMessage(StringUtils.handle("&cNo arenas available!"));
                    q.getUser().getPlayer().sendMessage(StringUtils.handle("&cNo arenas available!"));
                    return;
                }

                Practice.getMatchManager().startSolo(arena, MatchType.QUEUE, kit, new User[]{q.getUser()}, new User[]{user}, ranked);
                break;
            }
        }
    }



    public void leaveQueue(User user) {
        if (user.getQueue() == null) {
            user.getPlayer().sendMessage(StringUtils.handle("&cYou are not in a queue!"));
            return;
        }

        Queue queue = user.getQueue();
        stopQueueTimer(queue);
        queues.remove(queue);
        user.setQueue(null);
        user.getPlayer().sendMessage(StringUtils.handle("&aYou have left the queue!"));
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
