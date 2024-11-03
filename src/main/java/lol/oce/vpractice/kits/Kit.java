package lol.oce.vpractice.kits;

import lol.oce.vpractice.Practice;
import lol.oce.vpractice.arenas.Arena;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

    public void save() {
        // Save the kit to the config file
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".displayName", displayName);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".description", description);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".inventory", inventory);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".potionEffects", potionEffects);
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
}
