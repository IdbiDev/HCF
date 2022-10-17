package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class RankMenuInventory {

    public static Inventory inv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6*9, "§8Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }

        for(Map.Entry<Integer, Faction_Rank_Manager.Rank> rank : playertools.sortByPriority(playertools.getPlayerFaction(p)).entrySet()) {
            inv.addItem(RM_Items.rank(rank.getValue().name));
        }

        for (String name : namesGecim)
            inv.addItem(RM_Items.rank(name));

        inv.setItem(45, GUI_Items.back());
        inv.setItem(53, RM_Items.create());

        return inv;
    }
}
