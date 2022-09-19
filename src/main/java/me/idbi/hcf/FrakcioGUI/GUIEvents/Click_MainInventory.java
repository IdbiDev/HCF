package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_MainInventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equalsIgnoreCase("§8Faction Manager")) return;

        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        if(!(e.getWhoClicked() instanceof Player p)) return;

        //Player p = (Player) e.getWhoClicked();

         if(e.getCurrentItem().isSimilar(GUI_Items.rankManager())) {
            if(!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_RANKS)){
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
                GUI_Sound.playSound(p,"error");
                return;

            }
             GUI_Sound.playSound(p,"click");
             p.openInventory(RankMenuInventory.inv(p));
        }

        else if(e.getCurrentItem().isSimilar(GUI_Items.playerManager())) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
        }

        else if(e.getCurrentItem().isSimilar(IM_Items.inviteManager())) {
            e.getWhoClicked().openInventory(InviteManagerInventory.inv());
        }
    }
}
