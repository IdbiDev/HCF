package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_ManageRequests;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyMain  implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Manage Allies")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.manageRequests())) {
            e.getWhoClicked().openInventory(Alley_ManageRequests.inv());
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.manageAllies())) {

            e.getWhoClicked().openInventory(Ally_AllyListInventory.allyList((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }
    }
}
