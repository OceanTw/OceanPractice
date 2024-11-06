package lol.oce.hercules.lobby;

import lol.oce.hercules.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
public class LobbyItemManager {

    String queueItemName = StringUtils.handle("&5&l1v1 Queue &7(Right Click)");
    String createPartyName = StringUtils.handle("&5&lCreate Party &7(Right Click)");
    String settingsName = StringUtils.handle("&5&lSettings &7(Right Click)");
    String leaveQueueName = StringUtils.handle("&c&lLeave Queue &7(Right Click)");

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
