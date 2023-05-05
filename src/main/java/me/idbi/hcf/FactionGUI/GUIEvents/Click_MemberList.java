package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Menus.MainInventory;
import me.idbi.hcf.FactionGUI.Menus.MemberManageInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_MemberList implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§8Members")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().getType() != Material.SKULL_ITEM) return;

        String playerName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

        e.getWhoClicked().openInventory(MemberManageInventory.manage(((Player) e.getWhoClicked()), playerName));
        GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
    }
}
