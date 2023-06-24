package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.RP_Items;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Click_PermissionManager implements Listener {

    public static HashMap<FactionRankManager.Permissions, Boolean> getPermissions(Inventory inv) {
        HashMap<FactionRankManager.Permissions, Boolean> map = new HashMap<>();

        int counter = 0;
        for (ItemStack is : inv.getContents()) {
            counter++;
            if (is == null) continue;
            if (!is.hasItemMeta()) continue;

            if (is.getType() == Material.STAINED_GLASS_PANE) {
                if (is.getData().getData() == 14 || is.getData().getData() == 5) {
                    ItemStack mainItem = inv.getItem(counter - 10);
                    if (!mainItem.getItemMeta().hasEnchant(Enchantment.DURABILITY)) continue;

                    map.put(getPermission(mainItem.getEnchantmentLevel(Enchantment.DURABILITY)), getStatus(is));
                }
            }
        }

        return map;
    }

    public static FactionRankManager.Permissions getPermission(int level) {
        switch (level) {
            case 1: return FactionRankManager.Permissions.MANAGE_ALL;
            case 5: return FactionRankManager.Permissions.MANAGE_MONEY;
            case 3: return FactionRankManager.Permissions.MANAGE_INVITE;
            case 2: return FactionRankManager.Permissions.MANAGE_PLAYERS;
            case 4: return FactionRankManager.Permissions.MANAGE_RANKS;
            case 6: return FactionRankManager.Permissions.MANAGE_KICK;
            //default: Faction_Rank_Manager.Permissions.MANAGE_MONEY;
            default: throw new IllegalStateException("Unexpected value: " + level);
        }
    }

    public static boolean getStatus(ItemStack is) {
        if (is.getType() == Material.STAINED_GLASS_PANE) {
            return is.getData().getData() == 5;
        }
        return false;
    }
    /*
        MANAGE_ALL,      // Include faction manager
        MANAGE_MONEY,     // Basic role
        MANAGE_INVITE,    // Can or not invite other players
        MANAGE_RANKS,   // Can modify the faction home
        MANAGE_PLAYERS,  // Can withdraw from the faction balance
        MANAGE_KICK    // Can rename the faction
     */

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Permissions") || e.getView().getTitle().endsWith(" §8Ally Permissions"))
            return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(RP_Items.cancel(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().closeInventory();
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().isSimilar(RP_Items.save(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().closeInventory();

            String rankName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

            Faction f = Playertools.getPlayerFaction((Player) e.getWhoClicked());

            assert f != null;
            FactionRankManager.Rank rank = f.getRankByName(rankName);

            for (Map.Entry<FactionRankManager.Permissions, Boolean> hashMap : getPermissions(e.getInventory()).entrySet()) {
                rank.setPermission(hashMap.getKey(), hashMap.getValue());
            }
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.SUCCESS);
            f.rankCreateHistory.add(new HistoryEntrys.RankEntry(
                    rank.getName(),
                    ((Player) e.getWhoClicked()).getDisplayName(),
                    new Date().getTime(),
                    "modify"
            ));
        } else if (e.getCurrentItem().isSimilar(RP_Items.on())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.off());
        } else if (e.getCurrentItem().isSimilar(RP_Items.off())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.on());
        }
    }
}
