package me.idbi.hcf.Events.Enchants;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class EnchantInventory implements Listener {

    public static Inventory inv() {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8Enchant");

        for (int i = 0; i < inv.getSize(); i++) {
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
        im.setDisplayName(Messages.enchant_confirm_button.queue());
        im.setLore(Arrays.asList(
                "§5",
                Messages.confirm_button_lore.queue().replace("%xp_level%", Config.EnchantCost.asStr())
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack cancel() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (short) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Messages.enchant_cancel_button.queue());
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
        ArrayList<Enchantment> enchantmentArrayList = new ArrayList<Enchantment>() {{
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

        for (Enchantment enc : enchantmentArrayList) {
            if (enc.canEnchantItem(is)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onInv(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("§8Enchant")) {

            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.AIR) return;

            if (e.getCurrentItem().isSimilar(blackGlass()) || e.getCurrentItem().isSimilar(confirm()) || e.getCurrentItem().isSimilar(cancel())) {
                e.setCancelled(true);
            } else {
                if (!isEnchantable(e.getCurrentItem())) {
                    e.setCancelled(true);
                    return;
                }
            }

            //if (!e.getCurrentItem().hasItemMeta()) return;

            if (e.getCurrentItem().isSimilar(cancel())) {
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

            if (e.getSlot() == 13) {
                if (!isEnchantable(e.getCurrentItem())) {
                    e.setCancelled(true);
                    return;
                }

                if (!e.getCurrentItem().getEnchantments().isEmpty()) return;
            }

            if (e.getView().getTopInventory().getItem(13) == null) return;
            if (e.getView().getTopInventory().getItem(13).getType() == Material.AIR) return;


            if (e.getCurrentItem().isSimilar(confirm())) {
                Player p = (Player) e.getWhoClicked();
                if (p.getLevel() < Config.EnchantCost.asInt()) {
                    p.sendMessage(Messages.enchant_not_enough_xp.language(p).queue());
                    return;
                }

                ItemStack item = e.getView().getTopInventory().getItem(13);
                HCFRules.EnchantClass obj = HCFRules.randomEnchant();
                while (!obj.getEnchantment().canEnchantItem(item)) {
                    obj = HCFRules.randomEnchant();
                    //System.out.println(obj.getEnchantment());
                }
                for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    item.removeEnchantment(entry.getKey());
                }
                try {
                    EnchantmentStorageMeta meta_ench = (EnchantmentStorageMeta) item.getItemMeta();
                    meta_ench.addStoredEnchant(obj.getEnchantment(), obj.getLevel(), true);
                    item.setItemMeta(meta_ench);
                } catch (ClassCastException ignored) {
                    item.addEnchantment(obj.getEnchantment(), obj.getLevel());
                }
                if (Main.abilitiesLoaded) {
                    // item = Utils.updateLore(item);
                }
                p.setLevel(p.getLevel() - Config.EnchantCost.asInt());

                e.getWhoClicked().getInventory().addItem(item).values().forEach(itemstack ->
                        e.getWhoClicked().getWorld().dropItemNaturally(e.getWhoClicked().getLocation(), itemstack));

                e.getWhoClicked().closeInventory();

                //System.out.println(obj.getEnchantment().getName());
            }
        }
    }
}
