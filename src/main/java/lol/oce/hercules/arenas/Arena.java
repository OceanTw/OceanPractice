package lol.oce.hercules.arenas;

import lol.oce.hercules.Practice;
import lol.oce.hercules.utils.LocationUtils;
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

    final ChunkAPI chunkAPI = ChunkAPI.getInstance();

    public void restore() {
        for (ChunkSnapshot snapshot : snapshots) {
            for (Chunk chunk : getChunks()) {
                if (chunk != null) {
                    ChunkAPI.getInstance().restoreSnapshot(chunk, snapshot);
                }
            }
        }
    }

    public void save() {
        // Save the arena to the config file
        name = name.toLowerCase();
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

    public void takeChunkSnapshots() {
        if (chunkAPI == null) {
            throw new IllegalStateException("ChunkAPI instance is not initialized.");
        }

        if (corner1 == null || corner2 == null) {
            throw new IllegalStateException("Corner locations are not set for the arena.");
        }

        Chunk[] chunks = getChunks();
        for (Chunk chunk : chunks) {
            if (chunk != null) {
                ChunkSnapshot snapshot = chunkAPI.takeSnapshot(chunk);
                snapshots.add(snapshot);
            }
        }
    }
}