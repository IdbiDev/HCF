package me.idbi.hcf.FactionGUI.Menus;

import me.idbi.hcf.FactionGUI.Items.Ally_Items;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Ally_ManageAlly {

    public static Inventory inv(Player owner, Faction ally) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8Managing " + ally.getName() + " §8ally");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, Ally_Items.permissions());
        inv.setItem(15, Ally_Items.abort());
        inv.setItem(22, GUI_Items.back(owner));

        return inv;
    }
}
