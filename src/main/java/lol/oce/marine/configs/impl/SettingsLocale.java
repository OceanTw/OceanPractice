package lol.oce.marine.configs.impl;

import lol.oce.marine.configs.ConfigService;
import lol.oce.marine.configs.impl.handler.DataType;
import lol.oce.marine.configs.impl.handler.IDataAccessor;
import lol.oce.marine.utils.ConfigFile;
import lol.oce.marine.utils.LocationUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum SettingsLocale implements IDataAccessor {
    DEBUG("debug", DataType.BOOLEAN, "false"),
    MONGO("mongo-url", DataType.STRING, "mongodb://localhost:27017"),
    LOBBY_LOCATION("lobby-location", DataType.STRING, LocationUtils.serialize(new Location(Bukkit.getWorld("world"), 0, 100, 0, 0, 0)))
    ;

    private final String path;
    private final List<String> defaultValue = new ArrayList<>();
    private final DataType dataType;

    SettingsLocale(String path, DataType dataType, String... defaultValue) {
        this.path = path;
        this.defaultValue.addAll(Arrays.asList(defaultValue));
        this.dataType = dataType;
    }

    @Override
    public String getHeader() {
        return "";
    }

    @Override
    public ConfigFile getConfigFile() {
        return ConfigService.getInstance().getSettingsConfig();
    }
}
