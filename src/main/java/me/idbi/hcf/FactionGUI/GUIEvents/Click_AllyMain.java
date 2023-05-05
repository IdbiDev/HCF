package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.Ally_Items;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.FactionGUI.Menus.Ally_ManageRequests;
import me.idbi.hcf.FactionGUI.Menus.MainInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyMain implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Manage Allies")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.manageRequests())) {
            e.getWhoClicked().openInventory(Ally_ManageRequests.inv(((Player) e.getWhoClicked())));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.manageAllies())) {

            e.getWhoClicked().openInventory(Ally_AllyListInventory.allyList((Player) e.getWhoClicked()));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
        }
    }
}
