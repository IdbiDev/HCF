package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_Permissions;
import me.idbi.hcf.Tools.Objects.AllyFaction;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class Click_ManageAlly implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getView().getTitle().startsWith("§8Managing ") && e.getView().getTitle().endsWith(" §8ally"))) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Ally_AllyListInventory.allyList((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        String title = e.getView().getTitle();
        String factionName = title.replace("§8Managing ", "").replace(" §8ally", "");

        Player p = (Player) e.getWhoClicked();
        Faction f = Playertools.getPlayerFaction(p);
        assert f != null;
        AllyFaction ally = null;

        for (Map.Entry<Integer, AllyFaction> allies : f.getAllies().entrySet()) {
            if (allies.getValue().getAllyFaction().getName().equalsIgnoreCase(factionName)) {
                ally = allies.getValue();
            }
        }
        if (ally == null) return;

        if (e.getCurrentItem().isSimilar(Ally_Items.permissions())) {
            e.getWhoClicked().openInventory(Ally_Permissions.inv(((Player) e.getWhoClicked()), ally));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.abort())) {
            f.resolveFactionAlly(ally.getAllyFaction());
            e.getWhoClicked().closeInventory();
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.SUCCESS);
        }
    }
}
