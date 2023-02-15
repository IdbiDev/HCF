package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.Permissions;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Click_AllyPermissions implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" §8Ally Permissions")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;


        if (e.getCurrentItem().isSimilar(RP_Items.cancel(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().closeInventory();
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().isSimilar(RP_Items.save(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().closeInventory();

            String allyName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

            Faction f = playertools.getPlayerFaction((Player) e.getWhoClicked());
            assert f != null;

            AllyFaction ally = f.Allies.get(Objects.requireNonNull(playertools.getFactionByName(allyName)).id);

            for(Map.Entry<Permissions, Boolean> hashMap : getPermissions(e.getInventory()).entrySet()) {
                ally.setPermission(hashMap.getKey(), hashMap.getValue());
            }
            ((Player) e.getWhoClicked()).sendMessage("Saved");
            GUI_Sound.playSound((Player) e.getWhoClicked(),"success");
            return;
        }

        else if(e.getCurrentItem().isSimilar(RP_Items.on())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.off());
        }

        else if(e.getCurrentItem().isSimilar(RP_Items.off())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.on());
        }
    }

    public static HashMap<Permissions, Boolean> getPermissions(Inventory inv) {
        HashMap<Permissions, Boolean> map = new HashMap<>();

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

    public static Permissions getPermission(int level) {
        return switch (level) {
            case 1 -> Permissions.FRIENDLY_FIRE;
            case 2 -> Permissions.USEBLOCK;
            case 3 -> Permissions.VIEWITEMS;
            case 4 -> Permissions.BREAKBLOCK;
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
