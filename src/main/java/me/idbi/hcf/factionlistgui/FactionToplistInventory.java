package me.idbi.hcf.factionlistgui;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FactionToplistInventory {

    public static Inventory listInv(Player p, List<Faction> factions, String type, int currentPage) {
        if(type.equalsIgnoreCase("Online"))
            type = "Onlines";
        Inventory inv = Bukkit.createInventory(null, 6*9, "§8Online factions by " + type + " | Page " + currentPage);
        inv = fillItems(inv);


        inv.setItem(49, FactionToplistItems.currentPage(p, currentPage));
        if(FactionToplistTools.hasPrevious(factions, currentPage))
            inv.setItem(45, FactionToplistItems.previousPage(p, currentPage));

        if(FactionToplistTools.hasNext(factions, currentPage))
            inv.setItem(53, FactionToplistItems.nextPage(p, currentPage));

        FactionToplistTools.getPageFactions(p, factions, currentPage).forEach(inv::addItem);
        return inv;
    }

    public static Inventory topInv(Player p, List<Faction> factions, String type, int currentPage) {
        Inventory inv = Bukkit.createInventory(null, 6*9, "§8Top factions by " + type + " | Page " + currentPage);
        inv = fillItems(inv);

        inv.setItem(49, FactionToplistItems.currentPage(p, currentPage));
        if(FactionToplistTools.hasPrevious(factions, currentPage))
            inv.setItem(45, FactionToplistItems.previousPage(p, currentPage));

        if(FactionToplistTools.hasNext(factions, currentPage))
            inv.setItem(53, FactionToplistItems.nextPage(p, currentPage));

        FactionToplistTools.getPageFactions(p, factions, currentPage).forEach(inv::addItem);
        return inv;
    }

    private static Inventory fillItems(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 9) {
                inv.setItem(i, GUI_Items.blackGlass());
            }
            if(i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36) {
                inv.setItem(i, GUI_Items.blackGlass());
            }

            if(i >= 44) {
                inv.setItem(i, GUI_Items.blackGlass());
            }
        }

        return inv;
    }
}
