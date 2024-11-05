package lol.oce.hercules.kits;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.utils.ConsoleUtils;
import lol.oce.hercules.utils.EffectUtils;
import lol.oce.hercules.utils.InventoryUtils;
import lol.oce.hercules.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
            ItemStack helmet = ItemUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".helmet"));
            ItemStack chestplate = ItemUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".chestplate"));
            ItemStack leggings = ItemUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".leggings"));
            ItemStack boots = ItemUtils.deserialize(Practice.getKitsConfig().getConfiguration().getString("kits." + key + ".boots"));

            Kit kit = new Kit(key, displayName, description, inventory, helmet, chestplate, leggings, boots, potionEffects, arenas, enabled, editable, boxing, build, sumo, mapDestroyable, hunger, healthRegen, bedfight, fireball, enderpearlcd, ranked, icon);
            addKit(kit);
        }
    }

    public void createKit(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Kit name cannot be null");
        }
        Kit kit = new Kit(name, name, "", null, null, null, null, null, new PotionEffect[0], new Arena[0], true, true, false, false, false, false, false, false, false, false, false, false, Material.DIAMOND_SWORD);
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
            if (kit.getDisplayName().contains(displayName)) {
                return kit;
            }
        }
        return null;
    }

    public void setKitInventory(Kit kit, Player player) {
        kit.setInventory(player.getInventory());
        for (PotionEffect potionEffect : kit.getPotionEffects()) {
            PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 99999, potionEffect.getAmplifier());
            addPotionEffect(kit, newPotionEffect);
        }
    }

    public void updateSettings() {
        for (Kit kit : kits) {
            kit.save();
        }
    }
    public void removeKit(Kit kit) {
        kits.remove(kit);
        enabledKits.remove(kit);
        Practice.getKitsConfig().getConfiguration().set("kits." + kit.getName(), null);
    }

    private void addPotionEffect(Kit kit, PotionEffect potionEffect) {
        PotionEffect[] newPotionEffects = new PotionEffect[kit.getPotionEffects().length + 1];
        System.arraycopy(kit.getPotionEffects(), 0, newPotionEffects, 0, kit.getPotionEffects().length);
        newPotionEffects[kit.getPotionEffects().length] = potionEffect;
        kit.setPotionEffects(newPotionEffects);
    }
}
