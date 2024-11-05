package lol.oce.hercules.kits;

import lol.oce.hercules.Practice;
import lol.oce.hercules.arenas.Arena;
import lol.oce.hercules.utils.EffectUtils;
import lol.oce.hercules.utils.InventoryUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;

@AllArgsConstructor
@Getter
@Setter
public class Kit {
    String name;
    String displayName;
    String description;
    Inventory inventory;
    PotionEffect[] potionEffects;
    Arena[] arenas;
    boolean enabled;
    boolean editable;
    boolean boxing;
    boolean build;
    boolean sumo;
    boolean mapDestroyable;
    boolean hunger;
    boolean healthRegen;
    boolean bedfight;
    boolean fireball;
    boolean enderpearlcd;
    boolean ranked;
    Material icon;

    public void save() {
        // Save the kit to the config file
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".displayName", displayName);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".description", description);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".inventory", InventoryUtils.serialize(inventory));
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".potionEffects", EffectUtils.serialize(potionEffects));
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".enabled", enabled);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".editable", editable);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".boxing", boxing);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".build", build);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".sumo", sumo);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".mapDestroyable", mapDestroyable);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".hunger", hunger);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".healthRegen", healthRegen);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".bedfight", bedfight);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".fireball", fireball);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".enderpearlcd", enderpearlcd);
        Practice.getKitsConfig().save();
    }

    public void addArena(Arena arena) {
        // Add an arena to the kit
        Arena[] newArenas = new Arena[arenas.length + 1];
        System.arraycopy(arenas, 0, newArenas, 0, arenas.length);
        newArenas[arenas.length] = arena;
        arenas = newArenas;
    }
}
