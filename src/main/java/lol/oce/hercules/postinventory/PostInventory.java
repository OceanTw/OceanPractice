package lol.oce.hercules.postinventory;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class PostInventory {
    final Player player;
    final int hits;
    final int blockedHits;
    final double damageDealt;
    final double healingDone;
    final int blockedPlaced;
    final int potionsThrown;

    public void getUI() {
        player.getInventory().clear();
    }

    public UUID getUuid() {
        return player.getUniqueId();
    }
}
