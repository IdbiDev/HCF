package me.idbi.hcf.FactionGUI.Menus;

import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Items.RM_Items;
import me.idbi.hcf.FactionGUI.Items.RPrio_Items;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class RankMenuInventory {

    public static Inventory inv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, "§8Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }

        for (Map.Entry<Integer, FactionRankManager.Rank> rank : Playertools.sortByPriority(Playertools.getPlayerFaction(p)).entrySet()) {
            inv.addItem(RM_Items.rank(p, rank.getValue().getName()));
        }

        inv.setItem(8, RPrio_Items.priorityToggleButton(p));
        inv.setItem(45, GUI_Items.back(p));
        inv.setItem(53, RM_Items.create(p));

        return inv;
    }
}
