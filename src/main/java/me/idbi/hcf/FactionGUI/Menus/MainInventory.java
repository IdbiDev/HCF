package me.idbi.hcf.FactionGUI.Menus;

import me.idbi.hcf.FactionGUI.Items.Ally_Items;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Items.IM_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainInventory {

    public static Inventory mainInv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4 * 9, "§8Faction Manager");
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(0, Ally_Items.ally());
        inv.setItem(11, GUI_Items.rankManager(p));
        inv.setItem(13, IM_Items.inviteManager(p));
        inv.setItem(15, GUI_Items.playerManager(p));
        inv.setItem(35, GUI_Items.renameFaction(p));

        Faction faction = Playertools.getPlayerFaction(p);
        assert faction != null;
        inv.setItem(31, GUI_Items.factionStats(p, faction));
        inv.setItem(27, GUI_Items.histories(p));
        return inv;
    }
}
