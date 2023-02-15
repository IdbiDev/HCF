package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.FrakcioGUI.Items.RPrio_Items;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class RankPriorityInventory {

    public static Inventory officialInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 6*9, "§8Rank Priority Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            boolean okaysDonatesDontes = i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44;
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (okaysDonatesDontes)
                inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(8, RPrio_Items.rankManagerToggleButton());
        inv.setItem(45, RP_Items.cancel());
        inv.setItem(53, RP_Items.save());

        Faction f = playertools.getPlayerFaction(p);

        for(Map.Entry<Integer, Faction_Rank_Manager.Rank> map : playertools.sortByPriority(f).entrySet()) {
            inv.addItem(RPrio_Items.ranks(map.getValue().name));
        }

        return inv;
    }

    public static Inventory editableInventoryAddEnchant(Inventory inv, ItemStack is, int slot) {
        inv.setItem(slot, RPrio_Items.selected(is));

        return inv;
    }

    public static Inventory editableInventoryRemoveEnchant(Inventory inv, ItemStack is, int slot) {
        inv.setItem(slot, RPrio_Items.ranks(ChatColor.stripColor(is.getItemMeta().getDisplayName())));

        return inv;
    }
}
