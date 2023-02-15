package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_ManageRequests;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_ManageAlly;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyRequests implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Manage Requests")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Alley_MainInventory.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.manageRequests())) {
            e.getWhoClicked().openInventory(Alley_ManageRequests.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.sentRequests())) {

            e.getWhoClicked().openInventory(Ally_AllyListInventory.requestList((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }
    }
}
