package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.PM_Items;
import me.idbi.hcf.FrakcioGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.PlayerRankManagerInventory;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
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

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if(e.getCurrentItem().isSimilar(PM_Items.kick(((Player) e.getWhoClicked())))) {
            if(!playertools.hasPermission((Player) e.getWhoClicked(), Faction_Rank_Manager.Permissions.MANAGE_KICK)){
                e.getWhoClicked().sendMessage(Messages.no_permission_in_faction.language(((Player) e.getWhoClicked())).queue());
                GUI_Sound.playSound((Player) e.getWhoClicked(), "error");
                e.getWhoClicked().closeInventory();
                return;
            }
            String[] name = e.getView().getTitle().split(" ");
            e.getWhoClicked().openInventory(KickConfirm.inv(name[0]));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if(e.getCurrentItem().isSimilar(PM_Items.rankManager(((Player) e.getWhoClicked())))) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(e.getView().getTitle().split(" ")[0]);
            if(target.isOnline()) {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(((Player) e.getWhoClicked()), target.getPlayer()));
                GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            } else {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(((Player) e.getWhoClicked()), target));
                GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            }
            return;
        }
    }

}
