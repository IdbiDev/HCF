package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Alley_MainInventory {

    public static Inventory inv() {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Manage Allies");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, Ally_Items.manageRequests());
        inv.setItem(15, Ally_Items.manageAllies());
        inv.setItem(22, GUI_Items.back());

        return inv;
    }
}
