package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.Tools.Objects.AllyFaction;
import me.idbi.hcf.Tools.Objects.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Ally_Permissions {

    public static Inventory inv(Player p, AllyFaction ally) {
        Inventory inv = Bukkit.createInventory(null, 5 * 9, ally.getAllyFaction().name + " §8Ally Permissions");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(9, Ally_Items.permission_friendlyFire());
        inv.setItem(11, Ally_Items.permission_interact());
        inv.setItem(13, Ally_Items.permission_placeBlock());
        inv.setItem(15, Ally_Items.permission_breakBlocks());
        inv.setItem(17, Ally_Items.permission_inventoryAccess());

        ItemStack friendlyFire = ally.hasPermission(Permissions.FRIENDLY_FIRE) ? RP_Items.on() : RP_Items.off();
        ItemStack interact = ally.hasPermission(Permissions.INTERACT) ? RP_Items.on() : RP_Items.off();
        ItemStack placeBlock = ally.hasPermission(Permissions.PLACE_BLOCK) ? RP_Items.on() : RP_Items.off();
        ItemStack breakBlock = ally.hasPermission(Permissions.BREAK_BLOCK) ? RP_Items.on() : RP_Items.off();
        ItemStack inventoryAccess = ally.hasPermission(Permissions.INVENTORY_ACCESS) ? RP_Items.on() : RP_Items.off();

        inv.setItem(18, friendlyFire);
        inv.setItem(20, interact);
        inv.setItem(22, placeBlock);
        inv.setItem(24, breakBlock);
        inv.setItem(26, inventoryAccess);

        inv.setItem(44, RP_Items.save(p));
        inv.setItem(36, RP_Items.cancel(p));

        return inv;
    }
}
