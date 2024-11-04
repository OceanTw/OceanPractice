package lol.oce.hercules.players;

import com.mongodb.client.MongoCollection;
import lol.oce.hercules.Practice;
import lol.oce.hercules.database.MongoDB;
import lol.oce.hercules.utils.ConsoleUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private static final List<User> users = new ArrayList<>();
    private static final UserData userRepository = new UserData();

    public static void load(String playerName) {
        ConsoleUtils.info("Loading user: " + playerName);
        MongoCollection<Document> collection = MongoDB.getCollection("users");
        if (collection == null) {
            throw new IllegalStateException("Collection 'users' not found");
        }

        users.add(userRepository.loadUser(playerName));
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

    public User getUser(UUID uuid) {
        return users.stream().filter(user -> user.getPlayer().getUniqueId().toString().equals(uuid)).findFirst().orElse(null);
    }
}
