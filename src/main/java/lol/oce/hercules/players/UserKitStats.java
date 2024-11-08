package lol.oce.hercules.players;

import lol.oce.hercules.Practice;
import lol.oce.hercules.kits.Kit;
import lombok.Data;

import java.util.HashMap;

@Data
public class UserKitStats {

    final HashMap<Kit, Integer> elo = new HashMap<>();
    final HashMap<Kit, Integer> wins = new HashMap<>();
    final HashMap<Kit, Integer> losses = new HashMap<>();

    public int getElo(Kit kit) {
        return elo.get(kit);
    }

    public int getWins(Kit kit) {
        return wins.get(kit);
    }

    public int getLosses(Kit kit) {
        return losses.get(kit);
    }

    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Kit kit : elo.keySet()) {
            sb.append(kit.getName()).append(":").append(elo.get(kit)).append(":").append(wins.get(kit)).append(":").append(losses.get(kit)).append(",");
        }
        return sb.toString();
    }

    public UserKitStats deserialize(String data) {
        String[] kitData = data.split(",");
        if (kitData.length == 0) {
            return this;
        }
        for (String kit : kitData) {
            String[] kitStats = kit.split(":");
            Kit kitObj = Practice.getKitManager().getKit(kitStats[0]);
            elo.put(kitObj, Integer.parseInt(kitStats[1]));
            wins.put(kitObj, Integer.parseInt(kitStats[2]));
            losses.put(kitObj, Integer.parseInt(kitStats[3]));
            return this;
        }
        return null;
    }
}
