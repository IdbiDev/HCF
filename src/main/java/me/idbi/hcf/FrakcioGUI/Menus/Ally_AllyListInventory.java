package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class Ally_AllyListInventory {

    public static Inventory allyList(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4*9, "§8Allies");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i >= 27)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i == 9 || i == 17 || i == 18 || i == 26)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        //       név,     uuid
        Faction faction = playertools.getPlayerFaction(p);
        assert faction != null;
        for(AllyFaction ally : faction.Allies) {
            inv.addItem(Ally_Items.manageAlly(ally));
        }

        inv.setItem(31, GUI_Items.back());

        return inv;
    }

    public static Inventory requestList(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4*9, "§8Manage Requests");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i >= 27)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i == 9 || i == 17 || i == 18 || i == 26)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        //       név,     uuid
        Faction faction = playertools.getPlayerFaction(p);
        assert faction != null;
        for(Faction ally : faction.allyinvites.getInvitedAllies()) {
            inv.addItem(Ally_Items.requestedAlly(ally));
        }

        inv.setItem(31, GUI_Items.back());

        return inv;
    }
}
