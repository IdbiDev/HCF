package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.PM_Items;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MemberManageInventory {

    public static Inventory manage(String name) {
        Inventory inv = Bukkit.createInventory(null, 3*9, name + " Profile");

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, GUI_Items.blackGlass());

        inv.setItem(11, PM_Items.kick());
        inv.setItem(15, PM_Items.rankManager());

        inv.setItem(22, GUI_Items.back());

        return inv;
    }
}