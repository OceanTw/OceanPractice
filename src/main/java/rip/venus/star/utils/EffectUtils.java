package rip.venus.star.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EffectUtils {
    public String serialize(List<PotionEffect> effects) {
        StringBuilder serialized = new StringBuilder();
        for (PotionEffect effect : effects) {
            serialized.append(effect.getType().getName()).append(":").append(effect.getDuration()).append(":").append(effect.getAmplifier()).append(",");
        }
        return serialized.toString();
    }

    public static List<PotionEffect> deserialize(String serializedEffects) {
        if (serializedEffects == null || serializedEffects.isEmpty()) {
            return null;
        }

        List<PotionEffect> potionEffects = new ArrayList<>();
        String[] effects = serializedEffects.split(",");

        for (String effect : effects) {
            String[] parts = effect.split(":");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid potion effect format: " + effect);
            }

            PotionEffectType type = PotionEffectType.getByName(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

            potionEffects.add(new PotionEffect(type, duration, amplifier));
        }

        return potionEffects;
    }
}
