package lol.oce.vpractice.lobby;

import lol.oce.vpractice.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public class LobbyItemManager {

    String queueItemName = StringUtils.handle("&a&lQueue &7(Right Click)");

    public ItemStack getQueueItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtils.handle(queueItemName));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack () {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtils.handle(name));
        item.setItemMeta(meta);
        return item;
    }
}
