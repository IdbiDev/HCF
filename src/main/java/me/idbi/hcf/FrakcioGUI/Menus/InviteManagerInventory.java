package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InviteManagerInventory {

    public static Inventory inv() {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Invite Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, IM_Items.invitePlayers());
        inv.setItem(15, IM_Items.invitedPlayers());
        inv.setItem(22, GUI_Items.back());

        return inv;
    }

    public static Inventory invitedPlayers(Player p) {
        Inventory inv = Bukkit.createInventory(null, 4*9, "§8Invited Players");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            if(i == 9 || i == 17 || i == 18 || i == 26)
                inv.setItem(i, GUI_Items.blackGlass());
            if(i >= 27)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        Main.Faction faction = playertools.getPlayerFaction(p);

        assert faction != null;
        for (Player invitedPlayer : faction.invites.getInvitedPlayers()) {
            if(invitedPlayer == null) continue;

            inv.addItem(IM_Items.invitedPlayers(invitedPlayer));
        }

        inv.setItem(31, GUI_Items.back());


        return inv;
    }
}
