package lol.oce.marine.lobby;

import org.bukkit.entity.Player;

public class LobbyManager {

    public void giveItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(0, new LobbyItemManager().getQueueItem());
        player.getInventory().setItem(4, new LobbyItemManager().getCreatePartyItem());
        player.getInventory().setItem(8, new LobbyItemManager().getSettingsItem(player));
    }
}
