package me.idbi.hcf.events.Enchants.Anvil;

import me.idbi.hcf.HCF_Rules;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import java.util.Map;

public class AnvilEvent implements Listener {

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        // check whether the event has been cancelled by another plugin
        if (!e.isCancelled()) {

// not really necessary
            if (e.getWhoClicked() instanceof Player) {
                Player player = (Player) e.getWhoClicked();
                Inventory inv = e.getInventory();

// see if we are talking about an anvil here
                if (inv instanceof AnvilInventory) {
                    AnvilInventory anvil = (AnvilInventory) inv;
                    InventoryView view = e.getView();
                    int rawSlot = e.getRawSlot();

// compare raw slot to the inventory view to make sure we are in the upper inventory
                    if (rawSlot == view.convertSlot(rawSlot)) {
                        // 2 = result slot
                        if (rawSlot == 2) {
                            // all three items in the anvil inventory
                            ItemStack[] items = anvil.getContents();

// item in the left slot
                            ItemStack item1 = items[0];

// item in the right slot
                            ItemStack item2 = items[1];

// I do not know if this is necessary
                            if (item1 != null && item2 != null) {
                                int id1 = item1.getTypeId();
                                int id2 = item2.getTypeId();

// if the player is repairing something the ids will be the same
                                if (id1 != 0 && id1 == id2) {
                                    // item in the result slot

                                    ItemStack item3 = e.getCurrentItem();

                                    if(isAllowed(item3)) {

                                    } else {
                                        e.setCancelled(true);
                                    }

// check if there is an item in the result slot
                                    if (item3 != null) {
                                        ItemMeta meta = item3.getItemMeta();

// meta data could be null
                                        if (meta != null) {
                                            // get the repairable interface to obtain the repair cost
                                            if (meta instanceof Repairable) {
                                                Repairable repairable = (Repairable) meta;
                                                int repairCost = repairable.getRepairCost();

// can the player afford to repair the item
                                                if (player.getLevel() >= repairCost) {
// success
                                                } else {
// bugger
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isAllowed(ItemStack is) {
        for(Map.Entry<Enchantment, Integer> enc : is.getEnchantments().entrySet()) {
            if(HCF_Rules.allowedLevels.containsKey(enc.getKey())) {
                int value = HCF_Rules.allowedLevels.get(enc.getKey());
                int enchantLevel = enc.getValue();

                if(enchantLevel > value)
                    return false;
            } else
                return false;
        }
        return true;
    }
}