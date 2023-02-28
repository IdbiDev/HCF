package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class MemberListInventory {

    public static Inventory members(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4 * 9, "§8Members");

        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 27)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i == 9 || i == 17 || i == 18 || i == 26)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        //       név,     uuid
        Faction faction = Playertools.getPlayerFaction(p);
        assert faction != null;
        ArrayList<String> members = Playertools.getFactionMembers(faction.id);

        for (String memberName : members) {
            inv.addItem(GUI_Items.memberHead(p, memberName));
        }

        inv.setItem(31, GUI_Items.back(p));

        return inv;
    }
}
