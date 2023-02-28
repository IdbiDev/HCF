package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_ManageAlly;
import me.idbi.hcf.Tools.Objects.AllyFaction;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class Click_AllyManageList implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Allies")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Alley_MainInventory.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().getType() != Material.DIAMOND) return;

        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        Player p = (Player) e.getWhoClicked();
        Faction f = Playertools.getPlayerFaction(p);
        AllyFaction ally = null;
        assert f != null;

        for (Map.Entry<Integer, AllyFaction> allies : f.Allies.entrySet()) {
            if (allies.getValue().getAllyFaction().name.equalsIgnoreCase(name)) {
                ally = allies.getValue();
            }
        }
        if (ally == null) return;

        // ToDo: Message stb.
        e.getWhoClicked().openInventory(Ally_ManageAlly.inv((Player) e.getWhoClicked(), ally.getAllyFaction()));

        GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
    }
}
