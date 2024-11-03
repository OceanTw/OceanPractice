package lol.oce.vpractice.lobby;

import lol.oce.vpractice.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
public class LobbyItemManager {

    String queueItemName = StringUtils.handle("&9&lQueue &7(Right Click)");
    String createPartyName = StringUtils.handle("&9&lCreate Party &7(Right Click)");
    String settingsName = StringUtils.handle("&9&lSettings &7(Right Click)");

    public ItemStack getQueueItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(queueItemName);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getCreatePartyItem() {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(createPartyName);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSettingsItem(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwner(player.getName());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(settingsName);
        item.setItemMeta(meta);
        return item;
    }
}
