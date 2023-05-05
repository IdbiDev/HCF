package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.RP_Items;
import me.idbi.hcf.Tools.Objects.AllyFaction;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.Permissions;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Click_AllyPermissions implements Listener {

    public static HashMap<Permissions, Boolean> getPermissions(Inventory inv) {
        HashMap<Permissions, Boolean> map = new HashMap<>();

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

    public static Permissions getPermission(int level) {
        switch (level) {
            case 1: return Permissions.FRIENDLY_FIRE;
            case 2: return Permissions.INTERACT;
            case 3: return Permissions.PLACE_BLOCK;
            case 4: return Permissions.BREAK_BLOCK;
            case 5: return Permissions.INVENTORY_ACCESS;
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

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" §8Ally Permissions")) return;

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

            String allyName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

            Faction f = Playertools.getPlayerFaction((Player) e.getWhoClicked());
            assert f != null;

            AllyFaction ally = f.getAllies().get(Objects.requireNonNull(Playertools.getFactionByName(allyName)).getId());
            if (ally == null) return;

            for (Map.Entry<Permissions, Boolean> hashMap : getPermissions(e.getInventory()).entrySet()) {
                ally.setPermission(hashMap.getKey(), hashMap.getValue());
            }
            e.getWhoClicked().sendMessage(Messages.gui_priority_saved.language((Player) e.getWhoClicked()).queue());
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.SUCCESS);
        } else if (e.getCurrentItem().isSimilar(RP_Items.on())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.off());
        } else if (e.getCurrentItem().isSimilar(RP_Items.off())) {
            e.getInventory().setItem(e.getSlot(), RP_Items.on());
        }
    }
}
