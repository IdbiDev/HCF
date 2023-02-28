package me.idbi.hcf.HistoryGUI.Player;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerStatInventory {

    public static Inventory inv(Player p) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8" + p.getName() + "'s statistics");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, PlayerHistoryItems.stats(p));
        inv.setItem(15, PlayerHistoryItems.classStats(p));
        GUI_Sound.playSound(p, GUI_Sound.HCFSounds.SUCCESS);
        return inv;
    }
}
