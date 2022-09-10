package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PM_Items {

    public static ItemStack rankManager() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Player's Rank");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage rank!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack kick() {
        ItemStack is = new ItemStack(Material.REDSTONE_ORE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eKick Player");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to kick player!"
        ));
        is.setItemMeta(im);
        return is;
    }
}
