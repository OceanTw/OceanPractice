package lol.oce.marine.configs;

import lol.oce.marine.configs.impl.MessageLocale;
import lol.oce.marine.configs.impl.SettingsLocale;
import lol.oce.marine.utils.ConfigFile;
import lombok.Getter;

@Getter
public class ConfigService {
    @Getter
    private static ConfigService instance;
    private ConfigFile settingsConfig;
    private ConfigFile messagesConfig;
    private ConfigFile arenasConfig;
    private ConfigFile kitsConfig;
    private ConfigFile scoreboardsConfig;

    public void load() {
        settingsConfig = new ConfigFile("settings");
        messagesConfig = new ConfigFile("messages");
        arenasConfig = new ConfigFile("arenas");
        kitsConfig = new ConfigFile("kits");
        scoreboardsConfig = new ConfigFile("scoreboards");

        initialize();
    }

    public void reload() {
        settingsConfig.reload();
        messagesConfig.reload();
        arenasConfig.reload();
        kitsConfig.reload();
        scoreboardsConfig.reload();
    }

    public void initialize() {
        instance = this;
        SettingsLocale.DEBUG.load();
        MessageLocale.STATS_RESET.load();
    }
}
