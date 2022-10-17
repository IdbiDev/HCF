package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RPrio_Items {

    public static ItemStack ranks(String rankName) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§a" + rankName);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack limeGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack priorityToggleButton() {
        ItemStack is = new ItemStack(Material.EMPTY_MAP);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§ePriority Manager");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to change the order!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rankManagerToggleButton() {
        ItemStack is = new ItemStack(Material.EMPTY_MAP);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eRank Manager");
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to to go back!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack selected(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.setLore(Arrays.asList(
                "§5",
                "§aSELECTED!",
                "§7Click on another rank to swap their order!"

        ));
        is.setItemMeta(im);
        return is;
    }
}
