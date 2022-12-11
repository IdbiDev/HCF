package me.idbi.hcf.HistoryGUI.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Stats_Inventory implements Listener {

    @EventHandler
    public void onInv(InventoryClickEvent e) {
        if(e.getView().getTitle().endsWith("'s statistics")) {
            e.setCancelled(true);
        }
    }
}
