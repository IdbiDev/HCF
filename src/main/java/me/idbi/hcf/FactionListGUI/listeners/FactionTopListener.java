package me.idbi.hcf.FactionListGUI.listeners;

import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.FactionListGUI.FactionToplistInventory;
import me.idbi.hcf.FactionListGUI.FactionToplistItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class FactionTopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getView().getTitle().startsWith("§8Top factions by ") && e.getView().getTitle().contains(" | Page "))) return;
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;
        if(e.getCurrentItem().getType() == Material.COMPASS) return;
        if(e.getCurrentItem().getType() != Material.DOUBLE_PLANT) return;
        int page = Integer.parseInt(e.getView().getTitle().substring(e.getView().getTitle().length() - 1)); // string find?
        ArrayList<Faction> factions = Playertools.sortByPoints();
        String type = "Points";
        if(e.getView().getTitle().contains("Kills")) {
            type = "Kills";
            factions = Playertools.sortByKills();
        } else if(e.getView().getTitle().contains("Balance")) {
            factions = Playertools.sortByBalance();
            type = "Balance";
        }

        if(!(e.getWhoClicked() instanceof Player p)) return;
        if(FactionToplistItems.previousPage(p, page).isSimilar(e.getCurrentItem())) {
            p.openInventory(FactionToplistInventory.topInv(p, factions, type, Math.max(1, page - 1)));
        }
        if(FactionToplistItems.nextPage(p, page).isSimilar(e.getCurrentItem())) {
            p.openInventory(FactionToplistInventory.topInv(p, factions, type, page + 1));
        }
    }

    @EventHandler
    public void onClick2(InventoryClickEvent e) {
        if(!(e.getView().getTitle().startsWith("§8Online factions by ") && e.getView().getTitle().contains(" | Page "))) return;
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;
        if(e.getCurrentItem().getType() == Material.COMPASS) return;
        if(e.getCurrentItem().getType() != Material.DOUBLE_PLANT) return;
        int page = Integer.parseInt(e.getView().getTitle().substring(e.getView().getTitle().length() - 1)); // string find?
        ArrayList<Faction> factions = Playertools.sortByOnlineMembers();
        String type = "Online";
       if(e.getView().getTitle().contains("DTR")) {
            factions = Playertools.sortByDTR();
            type = "DTR";
        }

        if(!(e.getWhoClicked() instanceof Player p)) return;
        if(FactionToplistItems.previousPage(p, page).isSimilar(e.getCurrentItem())) {
            p.openInventory(FactionToplistInventory.listInv(p, factions, type, Math.max(1, page - 1)));
        }
        if(FactionToplistItems.nextPage(p, page).isSimilar(e.getCurrentItem())) {
            p.openInventory(FactionToplistInventory.listInv(p, factions, type, page + 1));
        }
    }
}
