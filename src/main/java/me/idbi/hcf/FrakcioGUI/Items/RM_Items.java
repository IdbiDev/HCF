package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RM_Items {

    public static ItemStack rank(String name) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§e" + name);
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage rank!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack create() {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§aCreate a New Rank");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to create a rank!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rename() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eRename Rank");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to rename rank!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permissionManager() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§ePermission Manager");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage permissions!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack deleteRank() {
        ItemStack is = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cDelete Rank");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to delete rank!"
        ));
        is.setItemMeta(im);
        return is;
    }
}
