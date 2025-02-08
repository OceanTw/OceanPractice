package lol.oce.marine.configs.impl;

import lol.oce.marine.configs.ConfigService;
import lol.oce.marine.configs.impl.handler.DataType;
import lol.oce.marine.configs.impl.handler.IDataAccessor;
import lol.oce.marine.utils.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum MessageLocale implements IDataAccessor {
    DEBUG("test", DataType.BOOLEAN, "Test","false"),
    ;

    private final String path;
    private final List<String> defaultValue = new ArrayList<>();
    private final DataType dataType;

    MessageLocale(String path, DataType dataType, String... defaultValue) {
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
