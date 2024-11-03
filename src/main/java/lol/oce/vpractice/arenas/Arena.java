package lol.oce.vpractice.arenas;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.utils.LocationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Chunk;
import org.bukkit.Location;

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
    final Chunk[] chunks = new Chunk[0];

    public void duplicate(int amount, int offsetX, int offsetZ) {
        // Ensure only SHARED arenas are duplicated
        if (type != ArenaType.SHARED) {
            throw new IllegalStateException("Cannot duplicate a SHARED arena");
        }

        // Check how many arenas are already duplicated
        int duplicated = 0;
        for (Arena arena : Practice.getArenaManager().getArenas()) {
            if (arena.getName().startsWith(name + "#")) {
                duplicated++;
            }
        }

        // Duplicate the arena
        for (int i = 0; i < amount; i++) {
            // Calculate offset for each duplicate
            int currentOffsetX = offsetX * (duplicated + i + 1);
            int currentOffsetZ = offsetZ * (duplicated + i + 1);

            // Create new locations with the offset applied
            Location newRedSpawn = redSpawn.clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newBlueSpawn = blueSpawn.clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newCorner1 = corner1.clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newCorner2 = corner2.clone().add(currentOffsetX, 0, currentOffsetZ);

            String newName = name + "#" + (duplicated + i + 1);
            Arena newArena = new Arena(newName, displayName, type, enabled, newRedSpawn, newBlueSpawn, newCorner1, newCorner2);
            Practice.getArenaManager().addArena(newArena);
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
        // Get the chunks that the arena occupies
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        Chunk[] chunks = new Chunk[(maxChunkX - minChunkX + 1) * (maxChunkZ - minChunkZ + 1)];
        int index = 0;
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                chunks[index++] = redSpawn.getWorld().getChunkAt(x, z);
            }
        }

        return chunks;
    }
}
