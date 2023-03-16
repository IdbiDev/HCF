package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.FrakcioGUI.Items.RPrio_Items;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RankPriorityInventory {

    public static Inventory officialInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, "§8Rank Priority Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            boolean okaysDonatesDontes = i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44;
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (okaysDonatesDontes)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(8, RPrio_Items.rankManagerToggleButton(p));
        inv.setItem(45, RP_Items.cancel(p));
        inv.setItem(53, RP_Items.save(p));

        Faction f = Playertools.getPlayerFaction(p);

        for (Map.Entry<Integer, FactionRankManager.Rank> map : Playertools.sortByPriority(f).entrySet()) {
            inv.addItem(RPrio_Items.ranks(p, map.getValue().getName()));
        }

        return inv;
    }

    public static Inventory editableInventoryAddEnchant(Player p, Inventory inv, ItemStack is, int slot) {
        inv.setItem(slot, RPrio_Items.selected(p, is));

        return inv;
    }

    public static Inventory editableInventoryRemoveEnchant(Player p, Inventory inv, ItemStack is, int slot) {
        inv.setItem(slot, RPrio_Items.ranks(p, ChatColor.stripColor(is.getItemMeta().getDisplayName())));

        return inv;
    }
}
