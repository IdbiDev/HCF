package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InviteManagerInventory {

    public static Inventory inv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8Invite Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, IM_Items.invitePlayers(p));
        inv.setItem(15, IM_Items.invitedPlayers(p));
        inv.setItem(22, GUI_Items.back(p));

        return inv;
    }

    public static Inventory invitedPlayers(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4 * 9, "§8Invited Players");

        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            if (i == 9 || i == 17 || i == 18 || i == 26)
                inv.setItem(i, GUI_Items.blackGlass());
            if (i >= 27)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        Faction faction = Playertools.getPlayerFaction(p);

        assert faction != null;
        for (Player invitedPlayer : faction.getInvites().getInvitedPlayers()) {
            if (invitedPlayer == null) continue;

            inv.addItem(IM_Items.invitedPlayer(invitedPlayer));
        }

        inv.setItem(31, GUI_Items.back(p));


        return inv;
    }
}
