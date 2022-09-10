package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MainInventory {

    public static Inventory mainInv() {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Faction Manager");
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, GUI_Items.rankManager());
        inv.setItem(15, GUI_Items.playerManager());

        return inv;
    }
}
