package lol.oce.vpractice.providers.manager;

import lol.oce.vpractice.utils.ConfigFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface IManager {
    ConfigFile getConfigFile();

    default void save(List<Value> values, String path) {
        for (Value value : values) {
            getConfigFile().getConfiguration().set(path + value.getName(), value.getObject());
        }
        getConfigFile().save();
    }

    default Set<String> getKeys(String path) {
        return Objects.requireNonNull(getConfigFile().getConfiguration().getConfigurationSection(path)).getKeys(false);
    }
}