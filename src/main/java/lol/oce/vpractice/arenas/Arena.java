package lol.oce.vpractice.arenas;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.utils.LocationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.Location;
import xyz.refinedev.spigot.api.chunk.ChunkAPI;
import xyz.refinedev.spigot.api.chunk.ChunkSnapshot;

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
    final List<ChunkSnapshot> snapshots = new ArrayList<>();

    public void restore() {
        for (ChunkSnapshot snapshot : snapshots) {
            for (Chunk chunk : getChunks()) {
                ChunkAPI.getInstance().restoreSnapshot(chunk, snapshot);
            }
        }
    }

    public void save() {
        // Save the arena to the config file
        String redSpawnLoc = LocationUtils.serialize(redSpawn);
        String blueSpawnLoc = LocationUtils.serialize(blueSpawn);
        String corner1Loc = LocationUtils.serialize(corner1);
        String corner2Loc = LocationUtils.serialize(corner2);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".displayName", displayName);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".type", type.name());
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".enabled", enabled);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".redSpawn", redSpawnLoc);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".blueSpawn", blueSpawnLoc);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".corner1", corner1Loc);
        Practice.getArenasConfig().getConfiguration().set("arenas." + name + ".corner2", corner2Loc);

        Practice.getArenasConfig().save();
    }

    public Chunk[] getChunks() {
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;
        return new Chunk[(maxChunkX - minChunkX + 1) * (maxChunkZ - minChunkZ + 1)];
    }

    public void takeChunkSnapshots() {
        // Get the chunks that the arena occupies
        Chunk[] chunks = getChunks();
        for (Chunk chunk : chunks) {
            ChunkSnapshot snapshot = ChunkAPI.getInstance().takeSnapshot(chunk);
            snapshots.add(snapshot);
        }
    }


}
