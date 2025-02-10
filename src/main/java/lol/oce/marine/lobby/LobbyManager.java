package lol.oce.marine.lobby;

import lol.oce.marine.Practice;
import lol.oce.marine.configs.impl.SettingsLocale;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LobbyManager {

    public void giveItems(Player player) {
        player.getInventory().clear();
        switch (Practice.getInstance().getUserManager().getUser(player.getUniqueId()).getStatus()) {
            case IN_LOBBY:
                player.getInventory().setItem(0, new LobbyItemManager().getQueueItem());
                player.getInventory().setItem(4, new LobbyItemManager().getCreatePartyItem());
                player.getInventory().setItem(8, new LobbyItemManager().getSettingsItem(player));
                break;
            case IN_QUEUE:
                player.getInventory().setItem(8, new LobbyItemManager().getLeaveQueueItem());
                break;
        }
    }

    public void setSpawn(Location location) {
        Practice.getInstance().getConfigService().getSettingsConfig().getConfiguration().set("lobby-location", LocationUtils.serialize(location));
        Practice.getInstance().getConfigService().getSettingsConfig().save();
    }
}
