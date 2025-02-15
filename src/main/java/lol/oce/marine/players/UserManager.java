package lol.oce.marine.players;

import com.mongodb.client.MongoCollection;
import lol.oce.marine.Practice;
import lol.oce.marine.configs.impl.SettingsLocale;
import lol.oce.marine.database.MongoDB;
import lol.oce.marine.match.Participant;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.LocationUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, User> users = new HashMap<>();
    private static final UserData userRepository = new UserData();

    public void load(UUID uuid) {
        ConsoleUtils.info("Loading user: " + uuid);
        MongoCollection<Document> collection = MongoDB.getCollection("users");
        if (collection == null) {
            throw new IllegalStateException("Collection 'users' not found");
        }

        User user = userRepository.loadUser(uuid);

        if (user != null) {
            users.put(uuid, user);
            ConsoleUtils.info("User loaded and added to list: " + uuid);
        } else {
            ConsoleUtils.warn("Failed to load user: " + uuid);
        }
    }

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User getUserFromDatabase(UUID uuid) {
        return userRepository.loadUser(uuid);
    }

    public List<User> getUsers(int startIndex, int amount) {
        return userRepository.getUsers(startIndex, amount);
    }

    public List<User> getUsers(String filter) {
        return userRepository.getUsers(filter);
    }

    public List<User> getUsers() {
        return userRepository.getUsers(0, Integer.MAX_VALUE);
    }

    public void resetUser(User user) {
        if (user.getStatus() == UserStatus.IN_MATCH) {
            user.getMatch().forfeit(user.getMatch().getParticipant(user));
        }
        if (user.getQueue() != null) {
            Practice.getInstance().getQueueManager().leaveQueue(user);
        }
        user.getPlayer().teleport(LocationUtils.deserialize(SettingsLocale.LOBBY_LOCATION.getString()));
        user.getPlayer().getInventory().clear();
        user.getPlayer().getInventory().setArmorContents(null);
        user.getPlayer().setHealth(20);
        user.getPlayer().setFoodLevel(20);
        user.getPlayer().setSaturation(20);
        user.getPlayer().setFireTicks(0);
        user.setStatus(UserStatus.IN_LOBBY);

        List<Player> matchPlayers = new ArrayList<>();
        if (user.getMatch() != null) {
            user.getMatch().getParticipants().forEach(participant -> matchPlayers.add(participant.getPlayer()));
            user.getMatch().getSpectators().forEach(player -> matchPlayers.add(player.getPlayer()));
        }
        for (Player player : matchPlayers) {
            user.getPlayer().showPlayer(player);
            player.showPlayer(user.getPlayer());
        }
        user.setMatch(null);

        Practice.getInstance().getLobbyManager().giveItems(user.getPlayer());
    }

    public void unload(UUID uuid) {
        User user = getUser(uuid);
        if (user != null) {
            if (user.getStatus() == UserStatus.IN_MATCH) {
                user.getMatch().forfeit(user.getMatch().getParticipant(user));
            }
            if (user.getQueue() != null) {
                Practice.getInstance().getQueueManager().leaveQueue(user);
            }
            userRepository.saveUser(user);
            users.remove(user);
            ConsoleUtils.info("User unloaded and removed from list: " + uuid);
        } else {
            ConsoleUtils.warn("Failed to unload user: " + uuid);
        }
    }
}