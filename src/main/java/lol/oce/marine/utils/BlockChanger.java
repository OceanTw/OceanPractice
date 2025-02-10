package lol.oce.marine.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lrxxh
 * @apiNote 1.8 - 1.21.4 easy to use util to be able to
 * set blocks blazingly fast
 */
public class BlockChanger {
    private static int MINOR_VERSION;
    private static JavaPlugin plugin;
    private static boolean debug;
    private static HashMap<Object, Object> worldCache;

    // NMS Classes
    private static Class<?> CRAFT_BLOCK_DATA;
    private static Class<?> LEVEL_HEIGHT_ACCESSOR;
    private static Class<?> CRAFT_WORLD;
    private static Class<?> WORLD_SERVER;
    private static Class<?> BLOCK;
    // NMS MethodHandles
    private static MethodHandle GET_STATE;
    private static MethodHandle GET_SECTIONS;
    private static MethodHandle GET_SECTION_INDEX;
    private static MethodHandle HAS_ONLY_AIR;
    private static MethodHandle GET_CHUNK_AT;
    private static MethodHandle GET_HANDLE_WORLD;
    private static MethodHandle GET_STATES;
    private static MethodHandle GET_AND_SET_UNCHECKED;
    private static MethodHandle GET;
    private static MethodHandle GET_COMBINED_ID;
    private static MethodHandle SET_TYPE;
    // NMS Fields
    private static Field NON_EMPTY_BLOCK_COUNT;
    // NMS Constructors
    private static Constructor<?> CRAFT_BLOCK_DATA_CONSTRUCTOR;
    private static Constructor<?> CHUNK_SECTION_CONSTRUCTOR;

    public static void load(JavaPlugin instance, boolean debug) {
        plugin = instance;
        MINOR_VERSION = getMinorVersion();
        BlockChanger.debug = debug;
        worldCache = new HashMap<>();

        init();
    }

    /**
     * Gets location's block-data using NMS.
     * This can be run async.
     *
     * @param location location to return block-data from
     * @return BlockData Block data found at given location
     */
    public static BlockData getBlockDataAt(Location location) {
        if (MINOR_VERSION == 8) return null;

        Object blockDataNMS = getBlockDataNMS(location);
        return getBlockDataFromNMS(blockDataNMS);
    }

    /**
     * Sets blocks block-data's using NMS.
     * This is suggested to be run async.
     *
     * @param world  World to set block in.
     * @param blocks Map of locations and ItemStacks to be set
     */
    public static void setBlocks(World world, Map<Location, ItemStack> blocks) {
        HashMap<Chunk, Object> chunkCache = new HashMap<>();

        for (Map.Entry<Location, ItemStack> entry : blocks.entrySet()) {
            if (MINOR_VERSION == 8) {
                setBlock(world, entry.getKey().getChunk(), entry.getKey(), entry.getValue(), chunkCache);
            }
        }

        for (Chunk chunk : chunkCache.keySet()) {
            world.refreshChunk(chunk.getX(), chunk.getZ());
        }
    }

    /**
     * Capture all blocks between 2 positions
     *
     * @param pos1 Position 1
     * @param pos2 Position 2
     * @return Snapshot This is needed to revert captured snapshot
     */
    public static Snapshot capture(Location pos1, Location pos2) {
        Location max = new Location(pos1.getWorld(), Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
        Location min = new Location(pos1.getWorld(), Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));

        Snapshot snapshot = new Snapshot();
        World world = max.getWorld();
        int minX = Math.min(min.getBlockX(), max.getBlockX());
        int minY = Math.min(min.getBlockY(), max.getBlockY());
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ());

        int maxX = Math.max(min.getBlockX(), max.getBlockX());
        int maxY = Math.max(min.getBlockY(), max.getBlockY());
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location = new Location(world, x, y, z);
                    if (MINOR_VERSION != 8) {
                        Object blockDataNMS = getBlockDataNMS(location);
                        BlockData blockData = getBlockDataFromNMS(blockDataNMS);

                        snapshot.add(new BlockSnapshot(location, blockData, blockDataNMS, location.getChunk()));
                    } else {
                        Block block = world.getBlockAt(location);
                        ItemStack itemStack = new ItemStack(block.getType());
                        itemStack.setData(block.getState().getData());

                        snapshot.add(new BlockSnapshotLegacy(location, location.getChunk(), itemStack));
                    }
                }
            }
        }

        return snapshot;
    }

    /**
     * Revert all changes from the snapshot, this runs async
     *
     * @param snapshot Snapshot you have captured
     */
    public static void revert(World world, Snapshot snapshot) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            long startTime = System.currentTimeMillis();
            HashMap<Chunk, Object> chunkCache = new HashMap<>();
            for (AbstractBlockSnapshot blockSnapshot : snapshot.snapshots) {
                setBlock(blockSnapshot, chunkCache);
            }

            for (Chunk chunk : chunkCache.keySet()) {
                world.refreshChunk(chunk.getX(), chunk.getZ());
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            debug("Snapshot revert time: " + duration + " ms (" + snapshot.snapshots.size() + ")");
        });
    }


    private static Object getBlockDataNMS(Location location) {
        try {
            Object nmsWorld = getWorldNMS(location.getWorld());

            Object nmsChunk = getChunkNMS(nmsWorld, location.getChunk(), false, null);

            int x = (int) location.getX();
            int y = location.getBlockY();
            int z = (int) location.getZ();

            Object cs = getSection(nmsChunk, y);

            return GET.invoke(GET_STATES.invoke(cs), x & 15, y & 15, z & 15);

        } catch (Throwable e) {
            debug("Error occurred while at #getBlock(World, Location) " + e.getMessage());
        }
        return null;
    }

    private static void setBlock(Location location, BlockData blockData, Chunk chunk, HashMap<Chunk, Object> chunkCache) {
        if (chunk == null) return;
        try {
            Object nmsBlockData = getBlockDataNMS(blockData);

            Object nmsWorld = getWorldNMS(location.getWorld());

            Object nmsChunk = getChunkNMS(nmsWorld, chunk, true, chunkCache);

            int x = (int) location.getX();
            int y = location.getBlockY();
            int z = (int) location.getZ();

            Object cs = getSection(nmsChunk, y);

            if (hasOnlyAir(cs, blockData)) return;

            GET_AND_SET_UNCHECKED.invoke(GET_STATES.invoke(cs), x & 15, y & 15, z & 15, nmsBlockData);

        } catch (Throwable e) {
            debug("Error occurred while at #setBlockNew(Location, BlockData) " + e.getMessage());
        }
    }

    private static void setBlock(AbstractBlockSnapshot snapshot, HashMap<Chunk, Object> chunkCache) {
        try {
            if (snapshot instanceof BlockSnapshot) {
                BlockSnapshot blockSnapshot = (BlockSnapshot) snapshot;
                Object nmsBlockData = blockSnapshot.blockDataNMS;
                BlockData blockData = (BlockData) blockSnapshot.blockData;
                Location location = snapshot.location;

                Chunk chunk = snapshot.chunk;
                Object nmsWorld = getWorldNMS(snapshot.location.getWorld());

                Object nmsChunk = getChunkNMS(nmsWorld, chunk, true, chunkCache);

                int x = (int) location.getX();
                int y = location.getBlockY();
                int z = (int) location.getZ();

                Object cs = getSection(nmsChunk, y);

                if (hasOnlyAir(cs, blockData)) return;

                GET_AND_SET_UNCHECKED.invoke(GET_STATES.invoke(cs), x & 15, y & 15, z & 15, nmsBlockData);
            } else {
                setBlock(snapshot.location.getWorld(), snapshot.chunk, snapshot.location, ((BlockSnapshotLegacy) snapshot).itemStack, chunkCache);
            }

        } catch (Throwable e) {
            debug("Error occurred while at #setBlockNew(Location, BlockData) " + e.getMessage());
        }
    }

    // 1.8
    private static void setBlock(World world, Chunk chunk, Location location, ItemStack itemStack, HashMap<Chunk, Object> chunkCache) {
        try {
            int x = (int) location.getX();
            int y = location.getBlockY();
            int z = (int) location.getZ();

            Object iBlockData = GET_COMBINED_ID.invoke(itemStack.getType().getId() + (itemStack.getData().getData() << 12));
            Object nmsWorld = getWorldNMS(world);
            Object nmsChunk = getChunkNMS(nmsWorld, chunk, true, chunkCache);
            Object cs = getSection(nmsChunk, y);
            SET_TYPE.invoke(cs, x & 15, y & 15, z & 15, iBlockData);

        } catch (Throwable e) {
            debug("Error occurred while at #setBlockAt(int, int, int, ItemStack) " + e.getCause());
        }
    }

    private static Object getWorldNMS(World world) {
        Object c = worldCache.get(world.getName());
        if (c != null) return c;
        try {
            Object craftWorld = CRAFT_WORLD.cast(world);
            Object worldServer = WORLD_SERVER.cast(GET_HANDLE_WORLD.invoke(craftWorld));
            worldCache.put(world.getName(), worldServer);

            return worldServer;
        } catch (Throwable e) {
            debug("Error occurred while at #getWorldNMS(World) " + e.getMessage());
        }
        return null;
    }

    private static Object getChunkNMS(Object world, Chunk chunk, boolean cache, HashMap<Chunk, Object> chunkCache) {
        if (cache) {
            Object c = chunkCache.get(chunk);
            if (c != null) return c;
        }

        try {
            Object nmsChunk = GET_CHUNK_AT.invoke(world, chunk.getX(), chunk.getZ());

            if (cache) chunkCache.put(chunk, nmsChunk);

            return nmsChunk;
        } catch (Throwable e) {
            debug("Error occurred while at #getChunkNMS(Object, Chunk, boolean) " + e.getMessage());
        }
        return null;
    }

    private static Object getLevelHeightAccessor(Object nmsChunk) {
        try {
            return LEVEL_HEIGHT_ACCESSOR.cast(nmsChunk);
        } catch (Throwable e) {
            debug("Error occurred while at #getLevelHeightAccessor(Object) " + e.getMessage());
        }
        return null;
    }

    private static BlockData getBlockDataFromNMS(Object blockDataNMS) {
        try {
            return (BlockData) CRAFT_BLOCK_DATA_CONSTRUCTOR.newInstance(blockDataNMS);
        } catch (Throwable e) {
            debug("Error occurred while at #getBlockDataFromNMS(Object) " + e.getMessage());
        }
        return null;
    }

    private static Object getBlockDataNMS(BlockData blockData) {
        try {
            return GET_STATE.invoke(CRAFT_BLOCK_DATA.cast(blockData));
        } catch (Throwable e) {
            debug("Error occurred while at #getBlockDataNMS(BlockData) " + e.getMessage());
        }
        return null;
    }

    private static Object getSection(Object nmsChunk, int index) {
        try {
            if (MINOR_VERSION != 8) {
                if (LEVEL_HEIGHT_ACCESSOR != null) {
                    Object LevelHeightAccessor = getLevelHeightAccessor(nmsChunk);

                    int i = (int) GET_SECTION_INDEX.invoke(LevelHeightAccessor, index);

                    return getSections(nmsChunk)[i];
                } else {
                    return getSections(nmsChunk)[index >> 4];
                }
            } else {
                int sectionIndex = index >> 4;
                Object cs = getSections(nmsChunk)[sectionIndex];
                if (cs == null) {
                    cs = CHUNK_SECTION_CONSTRUCTOR.newInstance(sectionIndex << 4, true);
                    getSections(nmsChunk)[sectionIndex] = cs;
                }

                return cs;
            }

        } catch (Throwable e) {
            debug("Error occurred while at #getSection(Object, int) " + e.getMessage());
        }

        return null;
    }

    private static void init() {
        String CRAFT_BUKKIT;
        String NET_MINECRAFT = "net.minecraft.";

        if (!supports(8)) {
            plugin.getLogger().info("Version Unsupported by BlockChanger");
            return;
        }

        if (MINOR_VERSION == 16 || MINOR_VERSION == 8) {
            NET_MINECRAFT = "net.minecraft.server." + plugin.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }
        if (supports(21)) {
            CRAFT_BUKKIT = "org.bukkit.craftbukkit.";
        } else {
            CRAFT_BUKKIT = "org.bukkit.craftbukkit." + plugin.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }

        if (MINOR_VERSION == 8) {
            BLOCK = loadClass(NET_MINECRAFT + "Block");
        }

        Class<?> I_BLOCK_DATA;

        if (MINOR_VERSION != 16 && MINOR_VERSION != 8) {
            I_BLOCK_DATA = loadClass(NET_MINECRAFT + "world.level.block.state.IBlockData");
        } else {
            I_BLOCK_DATA = loadClass(NET_MINECRAFT + "IBlockData");
        }
        debug("I_BLOCK_DATA Loaded");

        Class<?> DATA_PALETTE_BLOCK = null;
        if (MINOR_VERSION != 8) {
            if (MINOR_VERSION != 16) {
                DATA_PALETTE_BLOCK = loadClass(NET_MINECRAFT + "world.level.chunk.DataPaletteBlock");
            } else {
                DATA_PALETTE_BLOCK = loadClass(NET_MINECRAFT + "DataPaletteBlock");
            }
            debug("DATA_PALETTE_BLOCK Loaded");
        }

        Class<?> CHUNK;
        if (MINOR_VERSION != 16 && MINOR_VERSION != 8) {
            CHUNK = loadClass(NET_MINECRAFT + "world.level.chunk.Chunk");
        } else {
            CHUNK = loadClass(NET_MINECRAFT + "Chunk");
        }
        debug("CHUNK Loaded");

        Class<?> CHUNK_SECTION;

        if (MINOR_VERSION != 16 && MINOR_VERSION != 8) {
            CHUNK_SECTION = loadClass(NET_MINECRAFT + "world.level.chunk.ChunkSection");
        } else {
            CHUNK_SECTION = loadClass(NET_MINECRAFT + "ChunkSection");
        }
        debug("CHUNK_SECTION Loaded");

        if (MINOR_VERSION != 16 && MINOR_VERSION != 8) {
            WORLD_SERVER = loadClass(NET_MINECRAFT + "server.level.WorldServer");
        } else {
            WORLD_SERVER = loadClass(NET_MINECRAFT + "WorldServer");
        }
        debug("WORLD_SERVER Loaded");

        Class<?> WORLD;
        if (MINOR_VERSION != 16 && MINOR_VERSION != 8) {
            WORLD = loadClass(NET_MINECRAFT + "world.level.World");
        } else {
            WORLD = loadClass(NET_MINECRAFT + "World");
        }
        debug("WORLD Loaded");

        if (MINOR_VERSION != 8) {
            if (MINOR_VERSION != 16) {
                LEVEL_HEIGHT_ACCESSOR = loadClass(NET_MINECRAFT + "world.level.LevelHeightAccessor");
            } else {
                LEVEL_HEIGHT_ACCESSOR = loadClass(NET_MINECRAFT + "LevelHeightAccessor");
            }
            debug("LEVEL_HEIGHT_ACCESSOR Loaded");
        }

        CRAFT_WORLD = loadClass(CRAFT_BUKKIT + "CraftWorld");
        debug("CRAFT_WORLD Loaded");

        Class<?> i_CHUNK_ACCESS = null;

        if (MINOR_VERSION != 8) {
            if (MINOR_VERSION != 16) {
                i_CHUNK_ACCESS = loadClass(NET_MINECRAFT + "world.level.chunk.IChunkAccess");
            } else {
                i_CHUNK_ACCESS = loadClass(NET_MINECRAFT + "IChunkAccess");
            }
            debug("I_CHUNK_ACCESS Loaded");
        }

        if (MINOR_VERSION != 8) {
            CRAFT_BLOCK_DATA = loadClass(CRAFT_BUKKIT + "block.data.CraftBlockData");
            debug("CRAFT_BLOCK_DATA Loaded");
        }


        if (MINOR_VERSION != 8) {
            try {
                GET_STATE = getMethodHandle(CRAFT_BLOCK_DATA, "getState", I_BLOCK_DATA);
                debug("GET_STATE Loaded");
            } catch (Throwable e) {
                debug("GET_STATE didn't load " + e.getCause().getMessage());
            }
        }

        if (MINOR_VERSION != 8) {
            try {
                GET = getMethodHandle(DATA_PALETTE_BLOCK, "a", Object.class, int.class, int.class, int.class);
                debug("SET Loaded");
            } catch (Throwable e) {
                debug("SET didn't load " + e.getCause().getMessage());
            }
        }

        try {
            if (MINOR_VERSION != 8) {
                if (supports(21) || MINOR_VERSION == 16) {
                    GET_SECTIONS = getMethodHandle(i_CHUNK_ACCESS, "getSections", Array.newInstance(CHUNK_SECTION, 0).getClass());
                } else {
                    GET_SECTIONS = getMethodHandle(i_CHUNK_ACCESS, "d", Array.newInstance(CHUNK_SECTION, 0).getClass());
                }
            } else {
                GET_SECTIONS = getMethodHandle(CHUNK, "getSections", Array.newInstance(CHUNK_SECTION, 0).getClass());
            }

            debug("GET_SECTIONS Loaded");
        } catch (Throwable e) {
            debug("GET_SECTIONS didn't load " + e.getCause().getMessage());
        }

        if (MINOR_VERSION != 8) {
            try {
                if (supports(20)) {
                    GET_STATES = getMethodHandle(CHUNK_SECTION, "h", DATA_PALETTE_BLOCK);
                } else {
                    GET_STATES = getMethodHandle(CHUNK_SECTION, "i", DATA_PALETTE_BLOCK);
                }
                debug("GET_STATES Loaded");
            } catch (Throwable e) {
                debug("GET_STATES didn't load " + e.getCause().getMessage());
            }

            try {
                GET_AND_SET_UNCHECKED = getMethodHandle(DATA_PALETTE_BLOCK, "b", Object.class, int.class, int.class, int.class, Object.class);
                debug("SET Loaded");
            } catch (Throwable e) {
                debug("SET didn't load " + e.getCause().getMessage());
            }

            try {
                if (MINOR_VERSION == 21) {
                    GET_SECTION_INDEX = getMethodHandle(LEVEL_HEIGHT_ACCESSOR, "f", int.class, int.class);
                } else if (supports(17)) {
                    GET_SECTION_INDEX = getMethodHandle(LEVEL_HEIGHT_ACCESSOR, "e", int.class, int.class);
                }
                debug("GET_SECTION_INDEX Loaded");
            } catch (Throwable e) {
                debug("GET_SECTIONS didn't load " + e.getCause().getMessage());
            }

            try {
                if (supports(18)) {
                    HAS_ONLY_AIR = getMethodHandle(CHUNK_SECTION, "c", boolean.class);
                }
                debug("HAS_ONLY_AIR Loaded");
            } catch (Throwable e) {
                debug("GET_SECTIONS didn't load " + e.getCause().getMessage());
            }

            try {
                if (HAS_ONLY_AIR == null) {
                    assert CHUNK_SECTION != null;
                    if (MINOR_VERSION == 17) {
                        NON_EMPTY_BLOCK_COUNT = getDeclaredField(CHUNK_SECTION, "f");
                    } else if (MINOR_VERSION == 16) {
                        NON_EMPTY_BLOCK_COUNT = getDeclaredField(CHUNK_SECTION, "c");
                    }

                    debug("NON_EMPTY_BLOCK_COUNT Loaded");
                }
            } catch (Throwable e) {
                debug("NON_EMPTY_BLOCK_COUNT didn't load " + e.getCause().getMessage());
            }

        }

        try {
            if (MINOR_VERSION == 8) {
                GET_CHUNK_AT = getMethodHandle(WORLD, "getChunkAt", CHUNK, int.class, int.class);
                debug("GET_CHUNK_AT Loaded");
            } else {
                GET_CHUNK_AT = getMethodHandle(WORLD, "d", CHUNK, int.class, int.class);
                debug("GET_CHUNK_AT Loaded");
            }

        } catch (Throwable e) {
            debug("GET_CHUNK_AT didn't load " + e.getCause().getMessage());
        }

        try {
            GET_HANDLE_WORLD = getMethodHandle(CRAFT_WORLD, "getHandle", WORLD_SERVER);
            debug("GET_HANDLE_WORLD Loaded");
        } catch (Throwable e) {
            debug("GET_HANDLE_WORLD didn't load " + e.getCause().getMessage());
        }

        if (MINOR_VERSION != 8) {
            CRAFT_BLOCK_DATA_CONSTRUCTOR = getConstructor(CRAFT_BLOCK_DATA, I_BLOCK_DATA);
            debug("CRAFT_BLOCK_DATA_CONSTRUCTOR Loaded");
        }

        if (MINOR_VERSION == 8) {
            CHUNK_SECTION_CONSTRUCTOR = getConstructor(CHUNK_SECTION, int.class, boolean.class);
            debug("CHUNK_SECTION_CONSTRUCTOR Loaded");

            try {
                GET_COMBINED_ID = getMethodHandleStatic(BLOCK, "getByCombinedId", I_BLOCK_DATA, int.class);
                debug("GET_COMBINED_ID Loaded");
            } catch (Throwable e) {
                debug("GET_COMBINED_ID didn't load " + e.getCause().getMessage());
            }

            try {
                SET_TYPE = getMethodHandle(CHUNK_SECTION, "setType", void.class, int.class, int.class, int.class, I_BLOCK_DATA);
                debug("SET_TYPE Loaded");
            } catch (Throwable e) {
                debug("SET_TYPE didn't load " + e.getCause().getMessage());
            }
        }
    }

    private static boolean hasOnlyAir(Object cs, BlockData blockData) {
        try {
            if (HAS_ONLY_AIR != null) {
                if ((Boolean) HAS_ONLY_AIR.invoke(cs) && isAir(blockData.getMaterial())) return true;
            } else {
                if ((Short) NON_EMPTY_BLOCK_COUNT.get(cs) == 0 && isAir(blockData.getMaterial())) return true;
            }
        } catch (Throwable e) {
            debug("GET_HANDLE_WORLD didn't load " + e.getCause().getMessage());
        }

        return false;
    }

    private static boolean isAir(Material material) {
        switch (material) {
            case AIR:
            default:
                return false;
        }
    }

    private static MethodHandle getMethodHandle(Class<?> clazz, String methodName, Class<?> rtype, Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return lookup.findVirtual(clazz, methodName, MethodType.methodType(rtype, parameterTypes));
    }

    private static MethodHandle getMethodHandleStatic(Class<?> clazz, String methodName, Class<?> rtype, Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        return lookup.findStatic(clazz, methodName, MethodType.methodType(rtype, parameterTypes));
    }


    private static Object[] getSections(Object nmsChunk) {
        try {
            return (Object[]) GET_SECTIONS.invoke(nmsChunk);
        } catch (Throwable e) {
            debug("Error occurred while at #getSections(Object) " + e.getMessage());
        }
        return new Object[0];
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            debug("Error occurred while at #loadClass(String) " + e.getMessage());
        }
        return null;
    }

    private static Field getDeclaredField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            debug("Error occurred while at #getDeclaredField(Class<?>, String) " + e.getMessage());
        }
        return null;
    }

    private static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            debug("Error occurred while at invokeConstructor: " + e.getMessage());
        }
        return null;
    }

    private static void printAllMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.print("Method: " + method.getName());
            System.out.print(" | Return type: " + method.getReturnType().getSimpleName());
            System.out.print(" | Modifiers: " + Modifier.toString(method.getModifiers()));
            System.out.print(" | Parameters: ");
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 0) {
                System.out.print("None");
            } else {
                for (Parameter param : parameters) {
                    System.out.print(param.getType().getSimpleName() + " " + param.getName() + ", ");
                }
                System.out.print("\b\b");
            }
            System.out.println();
        }
    }

    private static void printAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.print("Field: " + field.getName());
            System.out.print(" | Type: " + field.getType().getSimpleName());
            System.out.print(" | Modifiers: " + Modifier.toString(field.getModifiers()));
            System.out.println();
        }
    }

    private static boolean supports(int version) {
        return MINOR_VERSION >= version;
    }

    private static int getMinorVersion() {
        String[] versionParts = plugin.getServer().getBukkitVersion().split("-")[0].split("\\.");
        if (versionParts.length >= 2) {
            return Integer.parseInt(versionParts[1]);
        }
        return 0;
    }

    private static void debug(String message) {
        if (debug) plugin.getLogger().info(message);
    }

    public static class Snapshot {
        protected List<AbstractBlockSnapshot> snapshots;

        protected Snapshot() {
            snapshots = new ArrayList<>();
        }

        protected void add(AbstractBlockSnapshot blockData) {
            snapshots.add(blockData);
        }
    }

    protected abstract static class AbstractBlockSnapshot {
        private final Location location;
        private final Chunk chunk;

        private AbstractBlockSnapshot(Location location, Chunk chunk) {
            this.location = location;
            this.chunk = chunk;
        }
    }

    private static class BlockSnapshot extends AbstractBlockSnapshot {
        private final Object blockData;
        private final Object blockDataNMS;

        private BlockSnapshot(Location location, Object blockData, Object blockDataNMS, Chunk chunk) {
            super(location, chunk);
            this.blockData = blockData;
            this.blockDataNMS = blockDataNMS;
        }

    }

    private static class BlockSnapshotLegacy extends AbstractBlockSnapshot {
        private final ItemStack itemStack;

        private BlockSnapshotLegacy(Location location, Chunk chunk, ItemStack itemStack) {
            super(location, chunk);
            this.itemStack = itemStack;
        }
    }
}