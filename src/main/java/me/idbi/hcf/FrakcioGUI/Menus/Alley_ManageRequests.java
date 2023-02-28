package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Alley_ManageRequests {

    public static Inventory inv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8Manage Requests");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, Ally_Items.request());
        inv.setItem(15, Ally_Items.sentRequests());
        inv.setItem(22, GUI_Items.back(p));

        return inv;
    }
}
