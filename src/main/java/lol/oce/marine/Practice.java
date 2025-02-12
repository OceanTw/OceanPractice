package lol.oce.marine;

import lol.oce.marine.adapters.ScoreboardAdapter;
import lol.oce.marine.arenas.ArenaManager;
import lol.oce.marine.commands.*;
import lol.oce.marine.commands.troll.DropCommand;
import lol.oce.marine.configs.ConfigService;
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
import lol.oce.marine.utils.BlockChanger;
import lol.oce.marine.utils.ConsoleUtils;
import lol.oce.marine.utils.scoreboards.Assemble;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Practice extends JavaPlugin {

    @Getter
    private static Practice instance;

    private ConfigService configService;
    private KitManager kitManager;
    private UserManager userManager;
    private ArenaManager arenaManager;
    private LobbyManager lobbyManager;
    private LobbyItemManager lobbyItemManager;
    private QueueManager queueManager;
    private RequestManager requestManager;
    private MatchManager matchManager;

    @Getter
    private static JavaPlugin plugin;

    @Getter
    private static boolean pluginLoaded = false;

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;

        configService = new ConfigService();
        configService.load();

        kitManager = new KitManager();
        userManager = new UserManager();
        arenaManager = new ArenaManager();
        lobbyManager = new LobbyManager();
        lobbyItemManager = new LobbyItemManager();
        queueManager = new QueueManager();
        matchManager = new MatchManager();
        requestManager = new RequestManager();

        BlockChanger.load(this, false);

        arenaManager.load();
        kitManager.load();

        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("practice").setExecutor(new MainCommand());
        getCommand("drop").setExecutor(new DropCommand());
        getCommand("duplicate").setExecutor(new DuplicateCommand());

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MatchListeners(), this);

        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(5);
        assemble.setup();

        ConsoleUtils.info("OceanPractice has been enabled! Waiting 5 seconds for plugin to load...");
        pluginLoaded = true;
    }

    @Override
    public void onDisable() {
        getLogger().info("Marine has been disabled!");
        matchManager.getMatches().forEach(Match::voidMatch);
        queueManager.getQueues().removeAll(queueManager.getQueues());
    }
}