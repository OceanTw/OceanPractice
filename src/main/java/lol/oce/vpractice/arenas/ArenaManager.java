package lol.oce.vpractice.arenas;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.kits.Kit;
import lol.oce.vpractice.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
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


            Arena arena = new Arena(key, displayName, type, enabled, redSpawn, blueSpawn, corner1, corner2);
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
