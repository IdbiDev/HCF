package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainInventory {

    public static Inventory mainInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4*9, "§8Faction Manager");
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, GUI_Items.rankManager());
        inv.setItem(13, IM_Items.inviteManager());
        inv.setItem(15, GUI_Items.playerManager());
        inv.setItem(35, GUI_Items.renameFaction());

        Main.Faction faction = playertools.getPlayerFaction(p);
        assert faction != null;
        inv.setItem(31, GUI_Items.factinoStats(faction));
        return inv;
    }
}
