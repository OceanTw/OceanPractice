package lol.oce.vpractice.players;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.utils.ConsoleUtils;

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
            ConsoleUtils.warn("Failed to load user with UUID " + uuid.toString());
        }
    }

    public void resetUser(User user) {
        // TODO: Teleport to set lobby location
        user.getPlayer().getInventory().clear();
        user.getPlayer().getInventory().setArmorContents(null);
        user.getPlayer().setHealth(20);
        user.getPlayer().setFoodLevel(20);
        user.getPlayer().setSaturation(20);

        user.getPlayer().getInventory().setItem(0, Practice.getKitManager().getKitSelector());
    }

    public User getUser(UUID uuid) {
        return users.stream().filter(user -> user.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }
}
