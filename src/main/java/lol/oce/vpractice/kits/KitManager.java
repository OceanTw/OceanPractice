package lol.oce.vpractice.kits;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.arenas.Arena;
import lol.oce.vpractice.utils.EffectUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class KitManager {

    List<Kit> enabledKits;
    List<Kit> kits;

    public void load() {
        // Load kits from config
        for (String key : Practice.getKitsConfig().getConfiguration().getConfigurationSection("kits").getKeys(false)) {
            String displayName = Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".displayName");
            String description = Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".description");
            Inventory inventory = (Inventory) Practice.getKitsConfig().getConfiguration().get("kits." + key + ".inventory");
            PotionEffect[] potionEffects = EffectUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".potionEffects"));
            Arena[] arenas = Practice.getArenasConfig().getConfiguration().getStringList("kits." + key + ".arenas").stream().map(Practice.getArenaManager()::getArena).toArray(Arena[]::new);
            boolean enabled = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".enabled");
            boolean editable = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".editable");
            boolean boxing = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".boxing");
            boolean build = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".build");
            boolean sumo = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".sumo");
            boolean mapDestroyable = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".mapDestroyable");
            boolean hunger = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".hunger");
            boolean healthRegen = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".healthRegen");
            boolean bedfight = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".bedfight");
            boolean fireball = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".fireball");
            boolean enderpearlcd = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".enderpearlcd");

            Kit kit = new Kit(key, displayName, description, inventory, potionEffects, arenas, enabled, editable, boxing, build, sumo, mapDestroyable, hunger, healthRegen, bedfight, fireball, enderpearlcd);
            kits.add(kit);
            if (enabled) {
                enabledKits.add(kit);
            }
        }
    }

    public Kit getKit(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public void setKitInventory(Kit kit, Player player) {
        kit.setInventory(player.getInventory());
        for (PotionEffect potionEffect : kit.getPotionEffects()) {
            PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 99999, potionEffect.getAmplifier());
            player.addPotionEffect(newPotionEffect);
        }
    }
}
