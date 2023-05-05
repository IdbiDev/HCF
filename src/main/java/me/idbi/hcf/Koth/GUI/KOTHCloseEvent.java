package me.idbi.hcf.Koth.GUI;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class KOTHCloseEvent implements Listener {

    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e) {
        if (!e.getView().getTitle().equals("§8KOTH Rewards")) return;
        if (!(e.getPlayer() instanceof Player)) return;
        Player p = (Player) e.getPlayer();
        if (Main.kothRewardsGUI.contains(e.getPlayer().getUniqueId())) return;
        KOTHItemManager.saveItems(KOTHInventory.saveableItems(e.getView().getTopInventory().getContents()));
        e.getPlayer().sendMessage(Messages.updated_koth_rewards.language(p).queue());
    }
}
