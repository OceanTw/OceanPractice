package rip.venus.star;

import rip.venus.star.adapters.ScoreboardAdapter;
import rip.venus.star.arenas.ArenaManager;
import rip.venus.star.commands.ArenaCommand;
import rip.venus.star.commands.DebugCommand;
import rip.venus.star.commands.DuelCommand;
import rip.venus.star.commands.KitCommand;
import rip.venus.star.commands.troll.DropCommand;
import rip.venus.star.duels.RequestManager;
import rip.venus.star.kits.KitManager;
import rip.venus.star.listeners.ItemListener;
import rip.venus.star.listeners.MatchListeners;
import rip.venus.star.listeners.PlayerListener;
import rip.venus.star.lobby.LobbyItemManager;
import rip.venus.star.lobby.LobbyManager;
import rip.venus.star.match.Match;
import rip.venus.star.match.MatchManager;
import rip.venus.star.match.QueueManager;
import rip.venus.star.players.UserManager;
import rip.venus.star.utils.ConfigFile;
import rip.venus.star.utils.scoreboards.Assemble;
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
        getLogger().info("Star has been disabled!");
        matchManager.getMatches().forEach(Match::voidMatch);
        queueManager.getQueues().removeAll(queueManager.getQueues());
    }
}