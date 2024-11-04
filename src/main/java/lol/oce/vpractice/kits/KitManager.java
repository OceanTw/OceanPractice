package lol.oce.vpractice.kits;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.arenas.Arena;
import lol.oce.vpractice.utils.ConsoleUtils;
import lol.oce.vpractice.utils.EffectUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
            boolean ranked = Practice.getKitsConfig().getConfiguration().getBoolean("kits." + key + ".ranked");
            Material icon = Material.getMaterial(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".icon"));

            Kit kit = new Kit(key, displayName, description, inventory, potionEffects, arenas, enabled, editable, boxing, build, sumo, mapDestroyable, hunger, healthRegen, bedfight, fireball, enderpearlcd, ranked, icon);
            kits.add(kit);
            if (enabled) {
                enabledKits.add(kit);
            }
        }
    }

    public void createKit(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Kit name cannot be null");
        }
        Kit kit = new Kit(name, name, "", null, new PotionEffect[0], new Arena[0], true, true, false, false, false, false, false, false, false, false, false, false, Material.DIAMOND_SWORD);
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

    public void setKitInventory(Kit kit, Player player) {
        kit.setInventory(player.getInventory());
        for (PotionEffect potionEffect : kit.getPotionEffects()) {
            PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 99999, potionEffect.getAmplifier());
            player.addPotionEffect(newPotionEffect);
        }
    }

    public void updateSettings() {
        for (Kit kit : kits) {
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".displayName", kit.getDisplayName());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".description", kit.getDescription());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".inventory", kit.getInventory());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".potionEffects", EffectUtils.serialize(kit.getPotionEffects()));
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".arenas", kit.getArenas());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".enabled", kit.isEnabled());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".editable", kit.isEditable());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".boxing", kit.isBoxing());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".build", kit.isBuild());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".sumo", kit.isSumo());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".mapDestroyable", kit.isMapDestroyable());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".hunger", kit.isHunger());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".healthRegen", kit.isHealthRegen());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".bedfight", kit.isBedfight());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".fireball", kit.isFireball());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".enderpearlcd", kit.isEnderpearlcd());
            Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName() + ".ranked", kit.isRanked());
        }
    }
    public void removeKit(Kit kit) {
        kits.remove(kit);
        enabledKits.remove(kit);
        Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName(), null);
    }

    public int getTotalKits() {
        return kits.size();
    }
}
