package lol.oce.vpractice.arenas;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@AllArgsConstructor
@Data
public class ArenaDuplicate {
    String originalName;
    int id;
    Location redSpawn;
    Location blueSpawn;
    Location corner1;
    Location corner2;
    boolean enabled;
}
