package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Ally_ManageAlly {

    public static Inventory inv(Faction ally) {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Managing " + ally.name + " §8ally");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, Ally_Items.permissions());
        inv.setItem(15, Ally_Items.abort());
        inv.setItem(22, GUI_Items.back());

        return inv;
    }
}
