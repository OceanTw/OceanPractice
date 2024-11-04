package lol.oce.vpractice;

import lol.oce.vpractice.arenas.ArenaManager;
import lol.oce.vpractice.commands.ArenaCommand;
import lol.oce.vpractice.commands.KitCommand;
import lol.oce.vpractice.kits.KitManager;
import lol.oce.vpractice.listeners.ItemListener;
import lol.oce.vpractice.listeners.PlayerListener;
import lol.oce.vpractice.lobby.LobbyItemManager;
import lol.oce.vpractice.lobby.LobbyManager;
import lol.oce.vpractice.players.UserManager;
import lol.oce.vpractice.utils.ConfigFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.ItemList;
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
    private static LobbyManager lobbyManager;
    @Getter
    private static LobbyItemManager lobbyItemManager;

    @Getter
    private static ConfigFile arenasConfig;
    @Getter
    private static ConfigFile kitsConfig;

    @Override
    public void onEnable() {
        instance = this;
        arenasConfig = new ConfigFile("arenas");
        kitsConfig = new ConfigFile("kits");

        kitManager = new KitManager();
        userManager = new UserManager();
        arenaManager = new ArenaManager();
        lobbyManager = new LobbyManager();
        lobbyItemManager = new LobbyItemManager();

        kitManager.load();
        arenaManager.load();

        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("kit").setExecutor(new KitCommand());

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("vPractice has been disabled!");
    }
}