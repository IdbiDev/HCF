package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_Permissions;
import me.idbi.hcf.FrakcioGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_ManageAlly implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().startsWith("§8Managing: ")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(Ally_AllyListInventory.allyList((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        String title = e.getView().getTitle();
        String factionName = title.replace("§8Managing: ", "").replace(" ally", "");

        Player p = (Player) e.getWhoClicked();
        Faction f = playertools.getPlayerFaction(p);
        AllyFaction ally = null;
        assert f != null;

        for (AllyFaction allies : f.Allies) {
            if(allies.getAllyFaction().name.equalsIgnoreCase(factionName)) {
                ally = allies;
            }
        }
        if(ally == null) return;

        if(e.getCurrentItem().isSimilar(Ally_Items.permissions())) {
            e.getWhoClicked().openInventory(Ally_Permissions.inv(ally));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if(e.getCurrentItem().isSimilar(Ally_Items.abort())) {
            f.resolveFactionAlly(ally.getAllyFaction());
            e.getWhoClicked().closeInventory();
            GUI_Sound.playSound((Player) e.getWhoClicked(), "success");
            return;
        }
    }
}
