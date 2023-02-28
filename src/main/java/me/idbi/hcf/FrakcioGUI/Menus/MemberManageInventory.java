package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.PM_Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MemberManageInventory {

    public static Inventory manage(Player owner, String name) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, name + " Profile");

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, GUI_Items.blackGlass());

        inv.setItem(11, PM_Items.kick(owner));
        inv.setItem(15, PM_Items.rankManager(owner));

        inv.setItem(22, GUI_Items.back(owner));

        return inv;
    }
}
