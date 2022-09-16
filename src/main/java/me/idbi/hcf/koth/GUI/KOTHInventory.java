package me.idbi.hcf.koth.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class KOTHInventory {


    public static Inventory kothInventory() {
        Inventory inv = Bukkit.createInventory(null, 6*9, "§8KOTH Rewards");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i == 0 || i == 1)
                inv.setItem(i, redGlass());
            else if(i <= 6)
                inv.setItem(i, whiteGlass());
            else if(i == 7 || i == 8)
                inv.setItem(i, redGlass());
            else if(i == 9 || i == 17)
                inv.setItem(i, redGlass());
            else if(i == 10 || i == 11 || i == 15 || i == 16)
                inv.setItem(i, whiteGlass());
            else if(i == 18 || i == 19 || i == 25 || i == 26 || i == 27 || i == 28 || i == 34 || i == 35)
                inv.setItem(i, whiteGlass());
            else if(i == 37 || i == 38 || i == 42 || i == 43)
                inv.setItem(i, whiteGlass());
            else if(i == 36 || i == 44)
                inv.setItem(i, redGlass());
            else if(i == 45 || i == 46)
                inv.setItem(i, redGlass());
            else if(i == 52 || i == 53)
                inv.setItem(i, redGlass());
            else if(i >= 47 && i <= 51)
                inv.setItem(i, whiteGlass());
        }

        inv.setItem(48, close());
        inv.setItem(50, save());

        if(KOTHItemManager.getRewards() == null) return inv;
        inv.addItem(KOTHItemManager.getRewards());
        return inv;
    }

    public static ItemStack save() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§aSave");
        im.setLore(Arrays.asList(
                "§5",
                "§7Close with saving rewards!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack whiteGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack redGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack close() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cClose");
        im.setLore(Arrays.asList(
                "§5",
                "§7Close without save rewards!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ArrayList<ItemStack> saveableItems(ItemStack[] items) {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (ItemStack item : items) {
            if(item == null) continue;
            if(!item.isSimilar(save()) && !item.isSimilar(close()) && !item.isSimilar(whiteGlass()) && !item.isSimilar(redGlass()))
                list.add(item);
        }

        return list;
    }

    public static boolean isGuiItem(ItemStack item) {
        if (item == null) return true;
        if (!item.isSimilar(save()) && !item.isSimilar(close()) && !item.isSimilar(whiteGlass()) && !item.isSimilar(redGlass()))
            return false;

        return true;
    }
}
