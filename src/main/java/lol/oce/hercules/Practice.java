package lol.oce.hercules;

import lol.oce.hercules.arenas.ArenaManager;
import lol.oce.hercules.commands.ArenaCommand;
import lol.oce.hercules.commands.DuelCommand;
import lol.oce.hercules.commands.KitCommand;
import lol.oce.hercules.database.MongoDB;
import lol.oce.hercules.duels.RequestManager;
import lol.oce.hercules.kits.KitManager;
import lol.oce.hercules.listeners.ItemListener;
import lol.oce.hercules.listeners.PlayerListener;
import lol.oce.hercules.lobby.LobbyItemManager;
import lol.oce.hercules.lobby.LobbyManager;
import lol.oce.hercules.match.MatchManager;
import lol.oce.hercules.match.QueueManager;
import lol.oce.hercules.players.UserManager;
import lol.oce.hercules.utils.ConfigFile;
import lol.oce.hercules.utils.ConsoleUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

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
    private static ConfigFile databaseConfig;
    @Getter
    private static ConfigFile arenasConfig;
    @Getter
    private static ConfigFile kitsConfig;

    @Override
    public void onEnable() {
        instance = this;
        arenasConfig = new ConfigFile("arenas");
        kitsConfig = new ConfigFile("kits");
        databaseConfig = new ConfigFile("database");

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

        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Hercules has been disabled!");
    }
}