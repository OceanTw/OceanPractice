package lol.oce.vpractice.arenas;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.utils.LocationUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    List<Arena> arenas;
    List<Arena> enabledArenas;

    public void save() {
        // Save the arenas to the config file
        for (Arena arena : arenas) {
            arena.save();
        }
    }

    public void addArena(Arena arena) {
        arenas.add(arena);
        if (arena.isEnabled()) {
            enabledArenas.add(arena);
        }
    }

    public void load() {
        // Load the arenas from the config file
        for (String key : Practice.getArenasConfig().getConfiguration().getConfigurationSection("arenas").getKeys(false)) {
            String displayName = Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".displayName");
            ArenaType type = ArenaType.valueOf(Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".type"));
            boolean enabled = Practice.getArenasConfig().getConfiguration().getBoolean("arenas." + key + ".enabled");
            Location redSpawn = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".redSpawn"));
            Location blueSpawn = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".blueSpawn"));
            Location corner1 = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".corner1"));
            Location corner2 = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString("arenas." + key + ".corner2"));

            // Load duplicates
            List<ArenaDuplicate> duplicatesList = new ArrayList<>();
            if (Practice.getArenasConfig().getConfiguration().contains("arenas." + key + ".duplicates")) {
                for (String duplicateKey : Practice.getArenasConfig().getConfiguration().getConfigurationSection("arenas." + key + ".duplicates").getKeys(false)) {
                    String basePath = "arenas." + key + ".duplicates." + duplicateKey;
                    String originalName = Practice.getArenasConfig().getConfiguration().getString(basePath + ".originalName");
                    int id = Practice.getArenasConfig().getConfiguration().getInt(basePath + ".id");
                    Location duplicateRedSpawn = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString(basePath + ".redSpawn"));
                    Location duplicateBlueSpawn = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString(basePath + ".blueSpawn"));
                    Location duplicateCorner1 = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString(basePath + ".corner1"));
                    Location duplicateCorner2 = LocationUtils.deserialize(Practice.getArenasConfig().getConfiguration().getString(basePath + ".corner2"));
                    boolean duplicateEnabled = Practice.getArenasConfig().getConfiguration().getBoolean(basePath + ".enabled");
                    ArenaDuplicate duplicate = new ArenaDuplicate(originalName, id, duplicateRedSpawn, duplicateBlueSpawn, duplicateCorner1, duplicateCorner2, duplicateEnabled);
                    duplicatesList.add(duplicate);
                }
            }
            ArenaDuplicate[] duplicates = duplicatesList.toArray(new ArenaDuplicate[0]);

            Arena arena = new Arena(key, displayName, type, enabled, redSpawn, blueSpawn, corner1, corner2, duplicates);
            arenas.add(arena);
            if (enabled) {
                enabledArenas.add(arena);
            }
        }
    }

    public Arena getArena(String name) {
        for (Arena arena : arenas) {
            if (arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }
}
