package rip.venus.star.kits;

import rip.venus.star.Practice;
import rip.venus.star.arenas.Arena;
import rip.venus.star.utils.ConsoleUtils;
import rip.venus.star.utils.EffectUtils;
import rip.venus.star.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@Getter
public class KitManager {

    List<Kit> enabledKits = new ArrayList<>();
    List<Kit> kits = new ArrayList<>();

    public void load() {
        // Load kits from config
        if (Practice.getKitsConfig().getConfiguration().getConfigurationSection("kits") == null) {
            ConsoleUtils.info("&cNo kits found in kits.yml, skipping kit load process...");
            return;
        }
        for (String key : Practice.getKitsConfig().getConfiguration().getConfigurationSection("kits").getKeys(false)) {
            String displayName = Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".displayName");
            String description = Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".description");
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
            boolean ranked = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".ranked");
            boolean freezeOnStart = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".freezeOnStart");
            Material icon = Material.getMaterial(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".icon"));

            Kit kit = new Kit(key, displayName, description, enabled, editable, boxing, build, sumo, mapDestroyable, hunger, healthRegen, bedfight, fireball, enderpearlcd, freezeOnStart, ranked, icon);
            if (EffectUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".potionEffects")) != null) {
                for (PotionEffect potionEffect : EffectUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".potionEffects"))) {
                    kit.getPotionEffects().add(potionEffect);
                }
            }
            if (!(Practice.getKitsConfig().getConfiguration().getStringList("kits." + key + ".arenas").size() <= 1)) {
                for (String arenaName : Practice.getKitsConfig().getConfiguration().getStringList("kits." + key + ".arenas")) {
                    Arena arena = Practice.getArenaManager().getArena(arenaName);
                    if (arena != null) {
                        kit.getArenas().add(arena);
                    }
                }
            } else {
                if (Practice.getKitsConfig().getConfiguration().getStringList("kits." + key + ".arenas").size() == 1) {
                    Arena arena = Practice.getArenaManager().getArena(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".arenas"));
                    if (arena != null) {
                        kit.getArenas().add(arena);
                    }
                }
            }
            if (Practice.getKitsConfig().getConfiguration().getStringList("kits." + key + ".contents") != null) {
                for (ItemStack item : ItemUtils.deserializeArray(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".contents"))) {
                    kit.getContents().add(item);
                }
            }
            if (Practice.getKitsConfig().getConfiguration().getStringList("kits." + key + ".armor") != null) {
                for (ItemStack item : ItemUtils.deserializeArray(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".armor"))) {
                    kit.getArmor().add(item);
                }
            }
            addKit(kit);
        }
    }

    public void createKit(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Kit name cannot be null");
        }
        Kit kit = new Kit(name, name, "", true, true, false, false, false, false, false, false, false, false, false, false, false, Material.DIAMOND_SWORD);
        kits.add(kit);
        enabledKits.add(kit);
    }

    public void addKit(Kit kit) {
        if (kit == null || kit.getName() == null) {
            throw new IllegalArgumentException("Kit or Kit name cannot be null");
        }
        kits.add(kit);
        if (kit.isEnabled()) {
            enabledKits.add(kit);
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

    public Kit getKitByDisplayName(String displayName) {
        for (Kit kit : kits) {
            if (displayName.contains(kit.getDisplayName())) {
                return kit;
            }
        }
        return null;
    }

    public void setKitInventory(Kit kit, Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            kit.addContents(item);
        }
        for (PotionEffect potionEffect : kit.getPotionEffects()) {
            PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 99999, potionEffect.getAmplifier());
            kit.addPotionEffect(newPotionEffect);
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            kit.addArmor(item);
        }
    }

    public void removeKit(Kit kit) {
        kits.remove(kit);
        enabledKits.remove(kit);
        Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName(), null);
    }
}
