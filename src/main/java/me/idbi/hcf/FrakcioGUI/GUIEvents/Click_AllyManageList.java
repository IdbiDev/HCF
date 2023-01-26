package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.*;
import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyManageList implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Allies")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if(!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if(e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(Alley_MainInventory.inv());
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if(e.getCurrentItem().getType() != Material.DIAMOND) return;

        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        Player p = (Player) e.getWhoClicked();
        Faction f = playertools.getPlayerFaction(p);
        AllyFaction ally = null;
        assert f != null;

        for (AllyFaction allies : f.Allies) {
            if(allies.getAllyFaction().name.equalsIgnoreCase(name)) {
                ally = allies;
            }
        }
        if(ally == null) return;

        // ToDo: Message stb.
        e.getWhoClicked().openInventory(Ally_ManageAlly.inv(ally.getAllyFaction()));

        GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
    }
}
