package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Ally_Permissions {

    public static Inventory inv(Player p, AllyFaction ally) {
        Inventory inv = Bukkit.createInventory(null, 5*9, ally.getAllyFaction().name + " §8Ally Permissions");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(10, Ally_Items.permission_friendlyFire());
        inv.setItem(12, Ally_Items.permission_useBlock());
        inv.setItem(14, Ally_Items.permission_breakBlocks());
        inv.setItem(16, Ally_Items.permission_viewItems());

        ItemStack friendlyFire = ally.hasPermission(Permissions.FRIENDLY_FIRE) ? RP_Items.on() : RP_Items.off();
        ItemStack useBlock = ally.hasPermission(Permissions.USEBLOCK) ? RP_Items.on() : RP_Items.off();
        ItemStack breakBlock = ally.hasPermission(Permissions.BREAKBLOCK) ? RP_Items.on() : RP_Items.off();
        ItemStack viewItems = ally.hasPermission(Permissions.VIEWITEMS) ? RP_Items.on() : RP_Items.off();

        inv.setItem(19, friendlyFire);
        inv.setItem(21, useBlock);
        inv.setItem(23, breakBlock);
        inv.setItem(25, viewItems);

        inv.setItem(44, RP_Items.save(p));
        inv.setItem(36, RP_Items.cancel(p));

        return inv;
    }
}
