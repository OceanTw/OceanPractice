package lol.oce.hercules.players;

import com.mongodb.client.MongoCollection;
import lol.oce.hercules.Practice;
import lol.oce.hercules.database.MongoDB;
import lol.oce.hercules.utils.ConsoleUtils;
import org.bson.Document;
import sun.audio.AudioStreamSequence;

import java.util.*;

public class UserManager {

    private static final List<User> users = new ArrayList<>();
    private static final UserData userRepository = new UserData();

    public void load(UUID uuid) {
        ConsoleUtils.info("Loading user: " + uuid);
        MongoCollection<Document> collection = MongoDB.getCollection("users");
        if (collection == null) {
            throw new IllegalStateException("Collection 'users' not found");
        }

        User user = userRepository.loadUser(uuid);
        if (user != null) {
            users.add(user);
            ConsoleUtils.info("User loaded and added to list: " + uuid);
        } else {
            ConsoleUtils.warn("Failed to load user: " + uuid);
        }
    }

    public void resetUser(User user) {
        // TODO: Teleport to set lobby location
        user.getPlayer().getInventory().clear();
        user.getPlayer().getInventory().setArmorContents(null);
        user.getPlayer().setHealth(20);
        user.getPlayer().setFoodLevel(20);
        user.getPlayer().setSaturation(20);

        Practice.getLobbyManager().giveItems(user.getPlayer());
    }

    public static User getUser(UUID uuid) {
        return users.stream()
                .filter(user -> {
                    if (user.getPlayer() == null) {
                        ConsoleUtils.warn("Player object is null for user: " + user);
                        return false;
                    }
                    return user.getPlayer().getUniqueId().equals(uuid);
                })
                .findFirst()
                .orElse(null);
    }

    public void unload(UUID uuid) {
        User user = getUser(uuid);
        if (user != null) {
            userRepository.saveUser(user);
            users.remove(user);
            ConsoleUtils.info("User unloaded and removed from list: " + uuid);
            if (user.getStatus() == UserStatus.IN_MATCH) {
                user.getMatch().forfeit(user);
            }
            if (user.getQueue() != null) {
                Practice.getQueueManager().leaveQueue(user);
            }
        } else {
            ConsoleUtils.warn("Failed to unload user: " + uuid);
        }
    }
}