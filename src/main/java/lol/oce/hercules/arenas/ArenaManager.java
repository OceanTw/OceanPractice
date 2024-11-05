package lol.oce.hercules.arenas;

import lol.oce.hercules.Practice;
import lol.oce.hercules.kits.Kit;
import lol.oce.hercules.utils.ConsoleUtils;
import lol.oce.hercules.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
public class ArenaManager {

    List<Arena> arenas = new ArrayList<>();
    List<Arena> enabledArenas = new ArrayList<>();

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

    public void duplicate(Arena arena, int amount, int offsetX, int offsetZ) {
        // Ensure only SHARED arenas are duplicated
        if (arena.getType() != ArenaType.SHARED) {
            throw new IllegalStateException("Cannot duplicate a SHARED arena");
        }

        // Check how many arenas are already duplicated
        int duplicated = 0;
        for (Arena arenas : Practice.getArenaManager().getArenas()) {
            if (arenas.getName().startsWith(arena.getName() + "#")) {
                duplicated++;
            }
        }

        // Duplicate the arena
        for (int i = 0; i < amount; i++) {
            // Calculate offset for each duplicate
            int currentOffsetX = offsetX * (duplicated + i + 1);
            int currentOffsetZ = offsetZ * (duplicated + i + 1);

            // Create new locations with the offset applied
            Location newRedSpawn = arena.getRedSpawn().clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newBlueSpawn = arena.getBlueSpawn().clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newCorner1 = arena.getCorner1().clone().add(currentOffsetX, 0, currentOffsetZ);
            Location newCorner2 = arena.getCorner2().clone().add(currentOffsetX, 0, currentOffsetZ);

            String newName = arena.getName() + "#" + (duplicated + i + 1);
            Arena newArena = new Arena(newName, arena.getDisplayName(), arena.getType(), arena.isEnabled(), newRedSpawn, newBlueSpawn, newCorner1, newCorner2);
            Practice.getArenaManager().addArena(newArena);
        }
    }

    public void load() {
        if (Practice.getArenasConfig().getConfiguration().getConfigurationSection("arenas") == null) {
            ConsoleUtils.info("&cNo arenas found in arenas.yml, skipping arena load process...");
            return;
        }
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

    public Arena getRandomArena(Kit kit) {
        List<Arena> availableArenas = new ArrayList<>();
        List<Arena> kitAllowedArenas = Arrays.asList(kit.getArenas());
        for (Arena arena : enabledArenas) {
            if (kitAllowedArenas.contains(arena)) {
                availableArenas.add(arena);
            }
        }
        if (availableArenas.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return availableArenas.get(random.nextInt(availableArenas.size()));
    }
}
