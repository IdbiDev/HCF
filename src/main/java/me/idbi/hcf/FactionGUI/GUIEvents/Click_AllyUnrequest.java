package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_ManageRequests;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyUnrequest implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Requests")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Alley_ManageRequests.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().getType() == Material.DIAMOND) {
            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            Player p = (Player) e.getWhoClicked();
            Faction f = Playertools.getPlayerFaction(p);
            assert f != null;

            Faction ally = null;

            for (Faction invitedAlly : f.getAllyInvites().getInvitedAllies()) {
                if (invitedAlly.getName().equalsIgnoreCase(name))
                    ally = invitedAlly;
            }

            if (ally == null) return;

            f.unInviteAlly(ally);
            ally.unInviteAlly(f);
            p.closeInventory();
            p.sendMessage(Messages.faction_uninvite_ally_success.language(p).setFaction(ally).queue());
            for(Player _player : ally.getOnlineMembers())
                _player.sendMessage(Messages.faction_uninvite_ally_target.language(_player).setFaction(f).queue());
        }
    }
}
