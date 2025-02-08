package lol.oce.marine.configs.impl.handler;

import lol.oce.marine.Practice;
import lol.oce.marine.utils.ConfigFile;

import java.util.Collections;
import java.util.List;

public interface IDataAccessor {

    Practice plugin = Practice.getInstance();

    default String getString() {
        return getConfigFile().getConfiguration().getString(getPath());
    }

    default List<String> getStringList() {
        return getConfigFile().getConfiguration().getStringList(getPath());
    }

    default int getInt() {
        return getConfigFile().getConfiguration().getInt(getPath());
    }

    default boolean getBoolean() {
        return getConfigFile().getConfiguration().getBoolean(getPath());
    }

    String getHeader();

    String getPath();

    List<String> getDefaultValue();

    DataType getDataType();

    ConfigFile getConfigFile();

    default void setHeader() {
        if (!getHeader().isEmpty()) {
            getConfigFile().getConfiguration().options().header(getHeader());
        }
    }

    default void setValue(String path, List<String> value, DataType dataType) {
        switch (dataType) {
            case STRING_LIST:
                getConfigFile().getConfiguration().set(path, value);
                break;
            case STRING:
                getConfigFile().getConfiguration().set(path, value.get(0));
                break;
            case INT:
                getConfigFile().getConfiguration().set(path, Integer.valueOf(value.get(0)));
                break;
            case BOOLEAN:
                getConfigFile().getConfiguration().set(path, Boolean.valueOf(value.get(0)));
                break;
        }
    }

    default void set(Object value) {
        getConfigFile().getConfiguration().set(getPath(), value);
        getConfigFile().save();
    }

    default void load() {
        setHeader();
        for (IDataAccessor accessor : this.getClass().getEnumConstants()) {
            ConfigFile configFile = accessor.getConfigFile();
            if (configFile == null) return;
            if (accessor.getConfigFile().getConfiguration().get(accessor.getPath()) == null) {
                setValue(accessor.getPath(), accessor.getDefaultValue(), accessor.getDataType());
            }
        }

        getConfigFile().save();
    }
}