package lol.oce.marine.arenas;

import com.mongodb.lang.Nullable;
import lol.oce.marine.Practice;
import lol.oce.marine.kits.Kit;
import lol.oce.marine.utils.BlockChanger;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.LocationUtils;
import lol.oce.marine.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

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
        if (arena.isEnabled() && !enabledArenas.contains(arena)) {
        	if (arena.getType() == ArenaType.STANDALONE && !arena.getName().contains("#")) {
        		return;
        	}
            enabledArenas.add(arena);
        }
    }

    public void duplicate(Arena arena, int amount, int offsetX, int offsetZ, @Nullable Player player) {
        if (arena.getType() == ArenaType.SHARED) {
            ConsoleUtils.info("&cYou cannot duplicate a shared arena.");
            return;
        }

        int minX = Math.min(arena.corner1.getBlockX(), arena.corner2.getBlockX());
        int minY = Math.min(arena.corner1.getBlockY(), arena.corner2.getBlockY());
        int minZ = Math.min(arena.corner1.getBlockZ(), arena.corner2.getBlockZ());

        int maxX = Math.max(arena.corner1.getBlockX(), arena.corner2.getBlockX());
        int maxY = Math.max(arena.corner1.getBlockY(), arena.corner2.getBlockY());
        int maxZ = Math.max(arena.corner1.getBlockZ(), arena.corner2.getBlockZ());

        Bukkit.getScheduler().runTask(Practice.getInstance(), () -> {
            Map<Location, ItemStack> blocks = new HashMap<>();
            for (int i = 1; i < amount; i++) {
                for (int x = minX; x <= maxX; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Location original = new Location(arena.redSpawn.getWorld(), x, y, z);
                            Location location = new Location(arena.redSpawn.getWorld(), x + (i * offsetX), y, z + (i * offsetZ));
                            Block block = arena.redSpawn.getWorld().getBlockAt(original);
                            ItemStack itemStack = new ItemStack(block.getType());
                            itemStack.setData(block.getState().getData());
                            blocks.put(location, itemStack);
                            if (block.getType() != Material.AIR) {
                                ConsoleUtils.debug(location + " " + itemStack);
                            }
                        }
                    }
                }
                int redX = arena.redSpawn.getBlockX() + (i * offsetX);
                int redZ = arena.redSpawn.getBlockZ() + (i * offsetZ);
                int blueX = arena.blueSpawn.getBlockX() + (i * offsetX);
                int blueZ = arena.blueSpawn.getBlockZ() + (i * offsetZ);
                Location redSpawn = new Location(arena.redSpawn.getWorld(), redX, arena.redSpawn.getBlockY(), redZ);
                Location blueSpawn = new Location(arena.blueSpawn.getWorld(), blueX, arena.blueSpawn.getBlockY(), blueZ);
                Arena duplicatedArena = new Arena(arena.getName() + "#" + i, arena.getDisplayName(), arena.getType(), arena.isEnabled(), redSpawn, blueSpawn, arena.corner1, arena.corner2);
                addArena(duplicatedArena);
                duplicatedArena.save();
                ConsoleUtils.info("&bDuplicated arena " + arena.getName() + " " + i + " times. Location: " + (minX + (i * offsetX)) + ", " + minY + ", " + (minZ + (i * offsetZ)));
            }
            BlockChanger.setBlocks(arena.redSpawn.getWorld(), blocks);
            ConsoleUtils.info("&aSuccessfully duplicated the arena.");
            if (player != null) {
                player.sendMessage(StringUtils.handle("&aSuccessfully duplicated the arena."));
            }
        });
    }

    public void load() {
        if (Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getConfigurationSection("arenas") == null) {
            ConsoleUtils.info("&cNo arenas found in arenas.yml, skipping arena load process...");
            return;
        }
        // Load the arenas from the config file
        for (String key : Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getConfigurationSection("arenas").getKeys(false)) {
            String displayName = Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".displayName");
            ArenaType type = ArenaType.valueOf(Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".type"));
            boolean enabled = Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getBoolean("arenas." + key + ".enabled");
            Location redSpawn = LocationUtils.deserialize(Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".redSpawn"));
            Location blueSpawn = LocationUtils.deserialize(Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".blueSpawn"));
            Location corner1 = LocationUtils.deserialize(Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".corner1"));
            Location corner2 = LocationUtils.deserialize(Practice.getInstance().getConfigService().getArenasConfig().getConfiguration().getString("arenas." + key + ".corner2"));


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
        List<Arena> kitAllowedArenas = kit.getArenas();
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
