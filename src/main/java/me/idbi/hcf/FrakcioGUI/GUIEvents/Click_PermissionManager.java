package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
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

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Permissions")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(RP_Items.cancel())) {
            e.getWhoClicked().closeInventory();
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().isSimilar(RP_Items.save())) {
            e.getWhoClicked().closeInventory();

            String rankName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

            Main.Faction f = playertools.getPlayerFaction((Player) e.getWhoClicked());

            assert f != null;
            Faction_Rank_Manager.Rank rank =  f.FindRankByName(rankName);

            for(Map.Entry<Faction_Rank_Manager.Permissions, Boolean> hashMap : getPermissions(e.getInventory()).entrySet()) {
                rank.setPermission(hashMap.getKey(), hashMap.getValue());
            }
            GUI_Sound.playSound((Player) e.getWhoClicked(),"success");
            Bukkit.broadcastMessage("Saved");
            f.rankCreateHistory.add(new HistoryEntrys.RankEntry(
                    rank.name,
                    ((Player) e.getWhoClicked()).getDisplayName(),
                    new Date().getTime(),
                    "modify"
            ));
            return;
        }

        else if(e.getCurrentItem().isSimilar(RP_Items.on())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.off());
        }

        else if(e.getCurrentItem().isSimilar(RP_Items.off())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.on());
        }
    }

    public static HashMap<Faction_Rank_Manager.Permissions, Boolean> getPermissions(Inventory inv) {
        HashMap<Faction_Rank_Manager.Permissions, Boolean> map = new HashMap<>();

        int counter = 0;
        for(ItemStack is : inv.getContents()) {
            counter++;
            if(is == null) continue;
            if(!is.hasItemMeta()) continue;

            if(is.getType() == Material.STAINED_GLASS_PANE) {
                if(is.getData().getData() == 14 || is.getData().getData() == 5) {
                    ItemStack mainItem = inv.getItem(counter - 10);
                    if (!mainItem.getItemMeta().hasEnchant(Enchantment.DURABILITY)) continue;

                    map.put(getPermission(mainItem.getEnchantmentLevel(Enchantment.DURABILITY)), getStatus(is));
                }
            }
        }

        return map;
    }

    public static Faction_Rank_Manager.Permissions getPermission(int level) {
        return switch (level) {
            case 1 -> Faction_Rank_Manager.Permissions.MANAGE_ALL;
            case 5 -> Faction_Rank_Manager.Permissions.MANAGE_MONEY;
            case 3 -> Faction_Rank_Manager.Permissions.MANAGE_INVITE;
            case 2 -> Faction_Rank_Manager.Permissions.MANAGE_PLAYERS;
            case 4 -> Faction_Rank_Manager.Permissions.MANAGE_RANKS;
            case 6 -> Faction_Rank_Manager.Permissions.MANAGE_KICK;
            //default -> Faction_Rank_Manager.Permissions.MANAGE_MONEY;
            default -> throw new IllegalStateException("Unexpected value: " + level);
        };
    }
    /*
        MANAGE_ALL,      // Include faction manager
        MANAGE_MONEY,     // Basic role
        MANAGE_INVITE,    // Can or not invite other players
        MANAGE_RANKS,   // Can modify the faction home
        MANAGE_PLAYERS,  // Can withdraw from the faction balance
        MANAGE_KICK    // Can rename the faction
     */

    public static boolean getStatus(ItemStack is) {
        if(is.getType() == Material.STAINED_GLASS_PANE) {
            if (is.getData().getData() == 5) {
                return true;
            }
        }
        return false;
    }
}
