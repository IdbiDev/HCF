package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_MainInventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equalsIgnoreCase("§8Faction Manager")) return;

        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        if(e.getCurrentItem().isSimilar(GUI_Items.rankManager())) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv((Player) e.getWhoClicked()));
        }

        else if(e.getCurrentItem().isSimilar(GUI_Items.playerManager())) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
        }

        else if(e.getCurrentItem().isSimilar(IM_Items.inviteManager())) {
            e.getWhoClicked().openInventory(InviteManagerInventory.inv());
        }
    }
}
