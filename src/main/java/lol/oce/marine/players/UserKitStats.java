package lol.oce.marine.players;

import lol.oce.marine.Practice;
import lol.oce.marine.kits.Kit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
public class UserKitStats {

    HashMap<Kit, Integer> elo;
    HashMap<Kit, Integer> wins;
    HashMap<Kit, Integer> losses;

    public UserKitStats() {
        elo = new HashMap<>();
        wins = new HashMap<>();
        losses = new HashMap<>();
    }

    public int getElo(Kit kit) {
        return elo.get(kit);
    }

    public int getWins(Kit kit) {
        return wins.get(kit);
    }

    public int getLosses(Kit kit) {
        return losses.get(kit);
    }

    // TODO: Convert this to JSON
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Kit kit : elo.keySet()) {
            sb.append(kit.getName()).append(":").append(elo.get(kit)).append(":").append(wins.get(kit)).append(":").append(losses.get(kit)).append(",");
        }
        return sb.toString();
    }

    public UserKitStats deserialize(String data) {
        if (data.isEmpty()) {
            return this;
        }
        String[] kitData = data.split(",");
        if (kitData.length == 0) {
            return this;
        }
        for (String kit : kitData) {
            String[] kitStats = kit.split(":");
            Kit kitObj = Practice.getInstance().getKitManager().getKit(kitStats[0]);
            if (kitStats.length != 4 || kitObj == null) {
                continue;
            }
            elo.put(kitObj, Integer.parseInt(kitStats[1]));
            wins.put(kitObj, Integer.parseInt(kitStats[2]));
            losses.put(kitObj, Integer.parseInt(kitStats[3]));
            return this;
        }
        return null;
    }
}
