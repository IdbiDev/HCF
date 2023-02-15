package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_ManageRequests;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class Click_AllyUnRequest implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Requests")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Alley_ManageRequests.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().getType() == Material.DIAMOND) {
            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            Player p = (Player) e.getWhoClicked();
            Faction f = playertools.getPlayerFaction(p);
            assert f != null;

            Faction ally = null;

            for (Faction invitedAlly : f.allyinvites.getInvitedAllies()) {
                if(invitedAlly.name.equalsIgnoreCase(name))
                    ally = invitedAlly;
            }

            if(ally == null) return;

            // ToDo: Message stb.
            f.unInviteAlly(ally);
            p.closeInventory();
            p.sendMessage("Elvileg unrequested!");
        }
    }
}
