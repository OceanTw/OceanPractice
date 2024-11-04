package lol.oce.hercules.match;

import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.players.User;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    List<Queue> queues = new ArrayList<>();

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
        queues.stream().forEach(q -> {
            if (q.kit == kit && q.ranked == ranked) {
                // Matchmaking logic
                if (ranked) {
                    // Ranked matchmaking logic
                    // the elo range is the user's elo - elo range and the user's elo + elo range;
                    int eloRange = queue.queueRange;
                    int userElo = user.getKitStats().getElo(kit);
                    int minElo = userElo - eloRange;
                    int maxElo = userElo + eloRange;
                    if (q.user.getKitStats().getElo(kit) >= minElo && q.user.getKitStats().getElo(kit) <= maxElo) {
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
}
