package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Date;

public class Click_InvitedPlayers implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("§8Invited Players")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(InviteManagerInventory.inv(((Player) e.getWhoClicked())));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
            String playerName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            Faction faction = Playertools.getPlayerFaction((Player) e.getWhoClicked());
            assert faction != null;
            Player p = Bukkit.getPlayer(playerName);

            if (p == null) return;

            faction.unInvitePlayer(p);
            faction.inviteHistory.add(new HistoryEntrys.InviteEntry(
                    ((Player) e.getWhoClicked()).getPlayer().getName(),
                    p.getName(),
                    new Date().getTime(),
                    false
            ));
            e.getWhoClicked().sendMessage(Messages.uninvite_executor.language(((Player) e.getWhoClicked())).setPlayer(p).queue());
            p.sendMessage(Messages.uninvite_target.language(p).setPlayer((Player) e.getWhoClicked()).queue());
            e.getWhoClicked().openInventory(InviteManagerInventory.invitedPlayers((Player) e.getWhoClicked()));
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.SUCCESS);
        }
    }
}
