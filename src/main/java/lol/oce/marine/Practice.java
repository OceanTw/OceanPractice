package lol.oce.marine;

import lol.oce.marine.adapters.ScoreboardAdapter;
import lol.oce.marine.arenas.ArenaManager;
import lol.oce.marine.commands.ArenaCommand;
import lol.oce.marine.commands.DebugCommand;
import lol.oce.marine.commands.DuelCommand;
import lol.oce.marine.commands.KitCommand;
import lol.oce.marine.commands.troll.DropCommand;
import lol.oce.marine.duels.RequestManager;
import lol.oce.marine.kits.KitManager;
import lol.oce.marine.listeners.ItemListener;
import lol.oce.marine.listeners.MatchListeners;
import lol.oce.marine.listeners.PlayerListener;
import lol.oce.marine.lobby.LobbyItemManager;
import lol.oce.marine.lobby.LobbyManager;
import lol.oce.marine.match.Match;
import lol.oce.marine.match.MatchManager;
import lol.oce.marine.match.QueueManager;
import lol.oce.marine.players.UserManager;
import lol.oce.marine.utils.ConfigFile;
import lol.oce.marine.utils.scoreboards.Assemble;
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
    private static LobbyManager lobbyManager;
    @Getter
    private static LobbyItemManager lobbyItemManager;
    @Getter
    private static QueueManager queueManager;
    @Getter
    private static RequestManager requestManager;
    @Getter
    private static MatchManager matchManager;

    @Getter
    private static ConfigFile settingsConfig;
    @Getter
    private static ConfigFile databaseConfig;
    @Getter
    private static ConfigFile arenasConfig;
    @Getter
    private static ConfigFile kitsConfig;

    @Getter
    private static ChunkAPI chunkAPI;

    @Getter
    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;
        chunkAPI = ChunkAPI.getInstance();
        arenasConfig = new ConfigFile("arenas");
        kitsConfig = new ConfigFile("kits");
        databaseConfig = new ConfigFile("database");
        settingsConfig = new ConfigFile("settings");

        databaseConfig.getConfiguration().options().copyDefaults(true);
        databaseConfig.save();
        settingsConfig.getConfiguration().options().copyDefaults(true);
        settingsConfig.save();

        kitManager = new KitManager();
        userManager = new UserManager();
        arenaManager = new ArenaManager();
        lobbyManager = new LobbyManager();
        lobbyItemManager = new LobbyItemManager();
        queueManager = new QueueManager();
        matchManager = new MatchManager();
        requestManager = new RequestManager();

        kitManager.load();
        arenaManager.load();

        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("drop").setExecutor(new DropCommand());

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MatchListeners(), this);

        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(5);
        assemble.setup();
    }

    @Override
    public void onDisable() {
        getLogger().info("Marine has been disabled!");
        matchManager.getMatches().forEach(Match::voidMatch);
        queueManager.getQueues().removeAll(queueManager.getQueues());
    }
}