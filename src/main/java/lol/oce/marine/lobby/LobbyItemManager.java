package lol.oce.marine.lobby;

import lol.oce.marine.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
public class LobbyItemManager {

    String queueItemName = StringUtils.handle("&b&l1v1 Queue &7(Right Click)");
    String createPartyName = StringUtils.handle("&b&lCreate Party &7(Right Click)");
    String settingsName = StringUtils.handle("&b&lSettings &7(Right Click)");
    String leaveQueueName = StringUtils.handle("&b&lLeave Queue &7(Right Click)");

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

    public ItemStack getLeaveQueueItem() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(leaveQueueName);
        item.setItemMeta(meta);
        return item;
    }
}
