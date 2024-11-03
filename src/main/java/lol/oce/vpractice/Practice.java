package lol.oce.vpractice;

import lol.oce.vpractice.arenas.ArenaManager;
import lol.oce.vpractice.commands.ArenaCommand;
import lol.oce.vpractice.kits.KitManager;
import lol.oce.vpractice.players.UserManager;
import lol.oce.vpractice.utils.ConfigFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.refinedev.spigot.api.chunk.ChunkAPI;

public class Practice extends JavaPlugin {

    @Getter
    private static Practice instance;
    @Getter
    private static KitManager kitManager;
    @Getter
    private static UserManager userManager;
    @Getter
    private static ArenaManager arenaManager;

    @Getter
    private static ConfigFile arenasConfig;
    @Getter
    private static ConfigFile kitsConfig;

    @Getter
    private static ChunkAPI chunkAPI;

    @Override
    public void onEnable() {
        instance = this;
        arenasConfig = new ConfigFile("arenas");
        kitsConfig = new ConfigFile("kits");

        kitManager = new KitManager();
        userManager = new UserManager();
        arenaManager = new ArenaManager();

        chunkAPI = new ChunkAPI();

        kitManager.load();
        arenaManager.load();

        getCommand("arena").setExecutor(new ArenaCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("vPractice has been disabled!");
    }
}