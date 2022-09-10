package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class RankManagerInventory {

    public static Inventory inv(String rankName) {
        Inventory inv = Bukkit.createInventory(null, 3*9, rankName + " Rank");

        for (int i = 0; i < inv.getSize(); i++)
                inv.setItem(i, GUI_Items.blackGlass());

        inv.setItem(11, RM_Items.permissionManager());
        inv.setItem(13, RM_Items.rename());
        inv.setItem(15, RM_Items.deleteRank());

        inv.setItem(22, GUI_Items.back());

        return inv;
    }
}
