package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_MainInventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equalsIgnoreCase(MainInventory.mainInv().getTitle())) return;

        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        if(e.getCurrentItem().isSimilar(GUI_Items.rankManager())) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv((Player) e.getWhoClicked()));
        }

        else if(e.getCurrentItem().isSimilar(GUI_Items.playerManager())) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
        }
    }
}
