package lol.oce.marine.arenas;

import lol.oce.marine.Practice;
import lol.oce.marine.utils.LocationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class Arena {
    String name;
    String displayName;
    ArenaType type;
    boolean enabled;
    Location redSpawn;
    Location blueSpawn;
    Location corner1;
    Location corner2;

    public void restore() {
        // TODO: Implement this method
    }

    public void save() {
        // Save the arena to the config file
        name = name.toLowerCase();
        String redSpawnLoc = LocationUtils.serialize(redSpawn);
        String blueSpawnLoc = LocationUtils.serialize(blueSpawn);
        String corner1Loc = LocationUtils.serialize(corner1);
        String corner2Loc = LocationUtils.serialize(corner2);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".displayName", displayName);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".type", type.name());
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".enabled", enabled);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".redSpawn", redSpawnLoc);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".blueSpawn", blueSpawnLoc);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".corner1", corner1Loc);
        Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().set("arenas." + name + ".corner2", corner2Loc);

        Practice.getInstance().getConfigService().getArenasConfig().save();
    }

    public Chunk[] getChunks() {
        if (corner1 == null || corner2 == null) {
            return new Chunk[0];
        }

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        List<Chunk> chunks = new ArrayList<>();
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                Chunk chunk = corner1.getWorld().getChunkAt(x, z);
                chunks.add(chunk);
            }
        }
        return chunks.toArray(new Chunk[0]);
    }
}
