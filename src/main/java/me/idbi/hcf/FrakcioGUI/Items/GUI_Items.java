package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GUI_Items {

    public static ItemStack rankManager() {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Ranks");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage ranks!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack blackGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack playerManager() {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Players");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage members!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack back() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cBack");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack memberHead(String name) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(name);
        im.setDisplayName("§e" + name);
        is.setItemMeta(im);
        return is;
    }
}
