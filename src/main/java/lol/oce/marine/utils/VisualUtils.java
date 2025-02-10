package lol.oce.marine.utils;

import org.bukkit.entity.Player;

public class VisualUtils {

    public static void playDeathAnimation(Player deadPlayer, Player target) {
        // TODO: Use packet events
        // play death animation
//        DataWatcher dataWatcher = ((CraftPlayer) deadPlayer).getHandle().getDataWatcher();
//        dataWatcher.watch(0, (byte) 0x08); // Set the entity state to "Dying"
//
//        // Create and send the PacketPlayOutEntityMetadata packet to update the NPC's metadata
//        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(deadPlayer.getEntityId(), dataWatcher, true);
//        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
    }
}
