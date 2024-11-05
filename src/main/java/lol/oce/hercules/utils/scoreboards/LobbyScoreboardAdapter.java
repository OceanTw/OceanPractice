package lol.oce.hercules.utils.scoreboards;

import lol.oce.hercules.utils.StringUtils;
import lol.oce.hercules.utils.scoreboards.assemble.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LobbyScoreboardAdapter implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return StringUtils.handle("&b&lAether &7│ &fPractice");
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add(StringUtils.line("&7", 10));
        lines.add(StringUtils.handle("&b&lPractice"));
        lines.add(StringUtils.handle("&f • Online: &b" + player.getServer().getOnlinePlayers().size()));
        lines.add(StringUtils.handle("&f • Playing"));
        lines.add(StringUtils.handle("&f • In Queue"));
        lines.add(StringUtils.handle("&7"));
        lines.add(StringUtils.handle("&baether.rip"));
        lines.add(StringUtils.line("&7", 10));
        return Collections.emptyList();
    }
}
