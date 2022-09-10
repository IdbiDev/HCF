package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankPermissionInventory;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_RankManager  implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains(" Rank")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv((Player) e.getWhoClicked()));
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.rename())) {
            // ToDo: Rename
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.permissionManager())) {
            e.getWhoClicked().openInventory(RankPermissionInventory.inv());
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.deleteRank())) {
            // ToDo: Delete Confirm GUI
            return;
        }
    }

    public static Faction_Rank_Manager.Rank getRank(String title) {
        switch (title) {
            case ""
        }

        return "";
    }
}
