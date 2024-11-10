package lol.oce.hercules.postinventory;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PostInventoryManager {

    List<PostInventory> postInventories = new ArrayList<>();

    public void createPostInventory(Player p1, int hits, int blockedHits, double damageDealt, double healingDone, int blockedPlaced, int potionsThrown) {
        PostInventory postInventory = new PostInventory(p1, hits, blockedHits, damageDealt, healingDone, blockedPlaced, potionsThrown);
        postInventory.getUI();
    }
}
