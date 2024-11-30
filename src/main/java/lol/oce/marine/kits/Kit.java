package lol.oce.marine.kits;

import lol.oce.marine.Practice;
import lol.oce.marine.arenas.Arena;
import lol.oce.marine.utils.EffectUtils;
import lol.oce.marine.utils.ItemUtils;
import lombok.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Kit {
    private String name;
    private String displayName;
    private String description;
    private List<ItemStack> contents = new ArrayList<>();
    private List<ItemStack> armor = new ArrayList<>();
    private List<PotionEffect> potionEffects = new ArrayList<>();
    private List<Arena> arenas = new ArrayList<>();
    private boolean enabled;
    private boolean editable;
    private boolean boxing;
    private boolean build;
    private boolean sumo;
    private boolean mapDestroyable;
    private boolean noHunger;
    private boolean healthRegen;
    private boolean bedFight;
    private boolean fireball;
    private boolean enderPearlCd;
    private boolean freezeOnStart;
    private boolean ranked;
    private Material icon;

    public Kit(String name, String displayName, String description
    , boolean enabled, boolean editable, boolean boxing, boolean build, boolean sumo, boolean mapDestroyable, boolean hunger, boolean healthRegen, boolean bedFight, boolean fireball, boolean enderPearlCd, boolean freezeOnStart, boolean ranked, Material icon) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.enabled = enabled;
        this.editable = editable;
        this.boxing = boxing;
        this.build = build;
        this.sumo = sumo;
        this.mapDestroyable = mapDestroyable;
        this.noHunger = hunger;
        this.healthRegen = healthRegen;
        this.bedFight = bedFight;
        this.fireball = fireball;
        this.enderPearlCd = enderPearlCd;
        this.freezeOnStart = freezeOnStart;
        this.ranked = ranked;
        this.icon = icon;

    }

    public void save() {
        // Save the kit to the config file
        name = name.toLowerCase();
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".displayName", displayName);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".description", description);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".enabled", enabled);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".editable", editable);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".boxing", boxing);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".build", build);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".sumo", sumo);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".mapDestroyable", mapDestroyable);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".hunger", noHunger);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".healthRegen", healthRegen);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".bedfight", bedFight);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".fireball", fireball);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".enderpearlcd", enderPearlCd);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".freezeOnStart", freezeOnStart);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".ranked", ranked);
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".icon", icon.name());
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".contents", ItemUtils.serialize(contents));
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".potionEffects", EffectUtils.serialize(potionEffects));
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".armor", ItemUtils.serialize(armor));
        List<String> arenaList = new ArrayList<>();
        for (Arena arena : this.arenas) {
            arenaList.add(arena.getName());
        }
        Practice.getKitsConfig().getConfiguration().set("kits." + name + ".arenas", arenaList);
        Practice.getKitsConfig().save();
    }

    public void addArena(Arena arena) {
        // Add an arena to the kit
        arenas.add(arena);
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        // Add a potion effect to the kit
        potionEffects.add(potionEffect);
    }

    public void addContents(ItemStack items) {
        // Add items to the kit's contents
        contents.add(items);
    }

    public void addArmor(ItemStack items) {
        // Add items to the kit's armor
        armor.add(items);
    }

    public ItemStack[] getContent() {
        return contents.toArray(new ItemStack[0]);
    }

    public ItemStack[] getArmorContent() {
        return armor.toArray(new ItemStack[0]);
    }
}
