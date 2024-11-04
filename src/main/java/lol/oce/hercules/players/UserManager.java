package lol.oce.hercules.players;

import lol.oce.hercules.Practice;
import lol.oce.hercules.utils.ConsoleUtils;

import java.util.List;
import java.util.UUID;

public class UserManager {

    List<User> users;
    UserRepository userRepository;

    public void load(UUID uuid) {
        User user = userRepository.loadUser(uuid.toString());
        if (user != null) {
            users.add(user);
        } else {
            ConsoleUtils.warn("Failed to load user with UUID " + uuid);
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

    public User getUser(UUID uuid) {
        return users.stream().filter(user -> user.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }
}
