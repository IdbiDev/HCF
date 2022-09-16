package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.PM_Items;
import me.idbi.hcf.FrakcioGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MemberManageInventory;
import me.idbi.hcf.FrakcioGUI.Menus.PlayerRankManagerInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_PlayerManage implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Profile")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
            return;
        }

        if(e.getCurrentItem().isSimilar(PM_Items.kick())) {
            String[] name = e.getView().getTitle().split(" ");
            e.getWhoClicked().openInventory(KickConfirm.inv(name[0]));
            return;
        }

        if(e.getCurrentItem().isSimilar(PM_Items.rankManager())) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(e.getView().getTitle().split(" ")[0]);
            if(target.isOnline()) {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(target.getPlayer()));
            } else {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(target));
            }
            return;
        }
    }

}
