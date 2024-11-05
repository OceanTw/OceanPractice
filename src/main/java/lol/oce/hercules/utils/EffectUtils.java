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

    public static PotionEffect[] deserialize(String serializedEffects) {
        if (serializedEffects == null || serializedEffects.isEmpty()) {
            return new PotionEffect[0];
        }

        String[] effects = serializedEffects.split(";");
        PotionEffect[] potionEffects = new PotionEffect[effects.length];

        for (int i = 0; i < effects.length; i++) {
            String[] parts = effects[i].split(":");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid potion effect format: " + effects[i]);
            }

            PotionEffectType type = PotionEffectType.getByName(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

            if (type != null) {
                potionEffects[i] = new PotionEffect(type, duration, amplifier);
            }
        }

        return potionEffects;
    }
}
