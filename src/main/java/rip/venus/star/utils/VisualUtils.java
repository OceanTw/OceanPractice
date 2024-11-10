package rip.venus.star.utils;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VisualUtils {

    public static void playDeathAnimation(Player deadPlayer, Player target) {
        // play death animation
        DataWatcher dataWatcher = ((CraftPlayer) deadPlayer).getHandle().getDataWatcher();
        dataWatcher.watch(0, (byte) 0x08); // Set the entity state to "Dying"

        // Create and send the PacketPlayOutEntityMetadata packet to update the NPC's metadata
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(deadPlayer.getEntityId(), dataWatcher, true);
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
    }
}
