package lol.oce.vpractice.arenas;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.utils.LocationUtils;
import lol.oce.vpractice.utils.SchematicUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

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
    ArenaDuplicate[] duplicates;

    public void duplicate() {
        SchematicUtils schematicUtils = new SchematicUtils();
        String filename = name + "_duplicate";

        // Save the current arena as a schematic
        schematicUtils.saveSchem(filename, corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ(),
                corner2.getBlockX(), corner2.getBlockY(), corner2.getBlockZ(), corner1.getWorld());

        for (int i = 0; i < 10; i++) {
            // Define the new location for the duplicated arena
            Location newLocation = new Location(corner1.getWorld(), corner1.getX() + 100 * (i + 1), corner1.getY(), corner1.getZ());

            // Create a new ArenaDuplicate object for the duplicated arena
            ArenaDuplicate duplicatedArena = new ArenaDuplicate(name + "_duplicate_" + (i + 1), duplicates.length + 1, null, null, null, null, enabled);

            // Paste the schematic at the new location and update the new arena's locations
            schematicUtils.pasteSchem(filename, newLocation, duplicatedArena);

            // Add the duplicated arena to the duplicates array
            ArenaDuplicate[] newDuplicates = new ArenaDuplicate[duplicates.length + 1];
            System.arraycopy(duplicates, 0, newDuplicates, 0, duplicates.length);
            newDuplicates[duplicates.length] = duplicatedArena;
            duplicates = newDuplicates;
        }

        // Save the duplicated arenas to the config
        save();
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

        // Save duplicates
        for (int i = 0; i < duplicates.length; i++) {
            ArenaDuplicate duplicate = duplicates[i];
            String basePath = "arenas." + name + ".duplicates." + i;
            Practice.getArenasConfig().getConfiguration().set(basePath + ".originalName", duplicate.getOriginalName());
            Practice.getArenasConfig().getConfiguration().set(basePath + ".id", duplicate.getId());
            Practice.getArenasConfig().getConfiguration().set(basePath + ".redSpawn", LocationUtils.serialize(duplicate.getRedSpawn()));
            Practice.getArenasConfig().getConfiguration().set(basePath + ".blueSpawn", LocationUtils.serialize(duplicate.getBlueSpawn()));
            Practice.getArenasConfig().getConfiguration().set(basePath + ".corner1", LocationUtils.serialize(duplicate.getCorner1()));
            Practice.getArenasConfig().getConfiguration().set(basePath + ".corner2", LocationUtils.serialize(duplicate.getCorner2()));
            Practice.getArenasConfig().getConfiguration().set(basePath + ".enabled", duplicate.isEnabled());
        }

        Practice.getArenasConfig().save();
    }
}
