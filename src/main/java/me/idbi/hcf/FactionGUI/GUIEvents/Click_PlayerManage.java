package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Items.PM_Items;
import me.idbi.hcf.FactionGUI.KickConfirm.KickConfirm;
import me.idbi.hcf.FactionGUI.Menus.MemberListInventory;
import me.idbi.hcf.FactionGUI.Menus.PlayerRankManagerInventory;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Playertools;
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
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().isSimilar(PM_Items.kick(((Player) e.getWhoClicked())))) {
            if (!Playertools.hasPermission((Player) e.getWhoClicked(), FactionRankManager.Permissions.MANAGE_KICK)) {
                e.getWhoClicked().sendMessage(Messages.no_permission_in_faction.language(((Player) e.getWhoClicked())).queue());
                GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.ERROR);
                e.getWhoClicked().closeInventory();
                return;
            }
            String[] name = e.getView().getTitle().split(" ");
            e.getWhoClicked().openInventory(KickConfirm.inv(name[0]));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(PM_Items.rankManager(((Player) e.getWhoClicked())))) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(e.getView().getTitle().split(" ")[0]);
            if (target.isOnline()) {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(((Player) e.getWhoClicked()), target.getPlayer()));
                GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
            } else {
                e.getWhoClicked().openInventory(PlayerRankManagerInventory.inv(((Player) e.getWhoClicked()), target));
                GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.CLICK);
            }
        }
    }

}
