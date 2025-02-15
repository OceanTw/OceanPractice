package lol.oce.marine.players;

import com.mongodb.lang.Nullable;
import lol.oce.marine.Practice;
import lol.oce.marine.configs.impl.MessageLocale;
import lol.oce.marine.match.Match;
import lol.oce.marine.match.Queue;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
    UUID uuid;
    UserKitStats kitStats;
    UserStatus status;
    Match match;
    Queue queue;

    public boolean isInMatch() {
        return status == UserStatus.IN_MATCH;
    }

    public boolean isInQueue() {
        return status == UserStatus.IN_QUEUE;
    }

    public boolean isInLobby() {
        return status == UserStatus.IN_LOBBY;
    }

    public boolean isEditingKit() {
        return status == UserStatus.IN_KIT_EDITOR;
    }

    @Nullable
    public Player getPlayer() {
        return (Player) Bukkit.getOfflinePlayer(uuid);
    }

    public void resetStats() {
        kitStats = new UserKitStats();
        ConsoleUtils.info("Resetting stats for " + uuid + "...");
        if (Bukkit.getPlayer(uuid) != null) {
            for (String message : MessageLocale.STATS_RESET.getStringList()) {
                Bukkit.getPlayer(uuid).sendMessage(StringUtils.handle(message));
            }
        }
    }
}
