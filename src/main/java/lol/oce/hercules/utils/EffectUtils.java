package lol.oce.hercules.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class EffectUtils {
    public String serialize(PotionEffect[] effects) {
        StringBuilder serialized = new StringBuilder();
        for (PotionEffect effect : effects) {
            serialized.append(effect.getType().getName()).append(":").append(effect.getDuration()).append(":").append(effect.getAmplifier()).append(",");
        }
        return serialized.toString();
    }

    public PotionEffect[] deserialize(String source) {
        String[] split = source.split(",");
        PotionEffect[] effects = new PotionEffect[split.length];
        for (int i = 0; i < split.length; i++) {
            String[] effectSplit = split[i].split(":");
            effects[i] = new PotionEffect(PotionEffectType.getByName(effectSplit[0]), Integer.parseInt(effectSplit[1]), Integer.parseInt(effectSplit[2]));
        }
        return effects;
    }
}
