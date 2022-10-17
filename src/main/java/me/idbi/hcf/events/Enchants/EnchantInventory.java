package me.idbi.hcf.events.Enchants;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf_abilitys.Enchantments.EncListeners.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class EnchantInventory implements Listener {

    @EventHandler
    public void onInv(InventoryClickEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("§8Enchant")) {

            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.AIR) return;

            if(e.getCurrentItem().isSimilar(blackGlass()) || e.getCurrentItem().isSimilar(confirm()) || e.getCurrentItem().isSimilar(cancel())) {
                e.setCancelled(true);
            } else {
                if(!isEnchantable(e.getCurrentItem())) {
                    e.setCancelled(true);
                    return;
                }
            }

            //if (!e.getCurrentItem().hasItemMeta()) return;

            if(e.getCurrentItem().isSimilar(cancel())) {
                if (e.getView().getTopInventory().getItem(13) == null) {
                    e.getWhoClicked().closeInventory();
                    return;
                }
                if (e.getView().getTopInventory().getItem(13).getType() == Material.AIR) {
                    e.getWhoClicked().closeInventory();
                    return;
                }

                ItemStack is = e.getView().getTopInventory().getItem(13);

                e.getWhoClicked().getInventory().addItem(is).values().forEach(itemstack ->
                        e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), itemstack));

                e.getWhoClicked().closeInventory();
            }

            if(e.getSlot() == 13) {
                if(!isEnchantable(e.getCurrentItem())) {
                    e.setCancelled(true);
                    return;
                }

                if(!e.getCurrentItem().getEnchantments().isEmpty()) return;
            }

            if (e.getView().getTopInventory().getItem(13) == null) return;
            if (e.getView().getTopInventory().getItem(13).getType() == Material.AIR) return;


            if (e.getCurrentItem().isSimilar(confirm())) {
                Player p = (Player) e.getWhoClicked();
                if(p.getLevel() < 20) {
                    //p.sendMessage(Messages.ENCHANT_NOT_ENOUGH_XP.queue());
                }

                ItemStack item = e.getView().getTopInventory().getItem(13);
                HCF_Rules.Enchant_Obj obj = HCF_Rules.randomEnchant();
                while (!obj.getEnchantment().canEnchantItem(item)) {
                    obj = HCF_Rules.randomEnchant();
                    System.out.println(obj.getEnchantment());
                }
                for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    item.removeEnchantment(entry.getKey());
                }

                item.addEnchantment(obj.getEnchantment(), obj.getLevel());
                if(Main.abilities_loaded)
                    item = Utils.updateLore(item);
                p.setLevel(p.getLevel() - 20);

                e.getWhoClicked().getInventory().addItem(item).values().forEach(itemstack ->
                        e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), itemstack));

                e.getWhoClicked().closeInventory();

                System.out.println(obj.getEnchantment().getName());
            }
        }
    }


    public static Inventory inv() {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Enchant");

        for(int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, blackGlass());
        }

        inv.setItem(13, new ItemStack(Material.AIR));
        inv.setItem(12, cancel());
        inv.setItem(14, confirm());

        return inv;
    }

    public static ItemStack blackGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack confirm() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Messages.ENCHANT_CONFIRM_BUTTON.queue());
        im.setLore(Arrays.asList(
                "§5",
                Messages.CONFIRM_BUTTON_LORE.queue().replace("%xp_level%", ConfigLibrary.Enchant_xp_cost.getValue())
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack cancel() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (short) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Messages.ENCHANT_CANCEL_BUTTON.queue());
        is.setItemMeta(im);
        return is;
    }

    public static boolean isEnchantable(ItemStack is) {
/*Protection
Sharpness
FireAspect
Feather Falling
Durability
Flame
Infinity
Knockback
Power
Efficiency
Punch
Silk Touch*/
        ArrayList<Enchantment> enchantmentArrayList = new ArrayList<>() {{
            add(Enchantment.PROTECTION_ENVIRONMENTAL);
            add(Enchantment.DAMAGE_ALL);
            add(Enchantment.FIRE_ASPECT);
            add(Enchantment.PROTECTION_FALL);
            add(Enchantment.DURABILITY);
            add(Enchantment.ARROW_FIRE);
            add(Enchantment.ARROW_INFINITE);
            add(Enchantment.KNOCKBACK);
            add(Enchantment.ARROW_DAMAGE);
            add(Enchantment.DIG_SPEED);
            add(Enchantment.ARROW_KNOCKBACK);
            add(Enchantment.SILK_TOUCH);
        }};

        for(Enchantment enc : enchantmentArrayList) {
            if(enc.canEnchantItem(is)) {
                return true;
            }
        }

        return false;
    }
}
