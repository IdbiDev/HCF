package me.idbi.hcf.koth.GUI;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KOTHCloseEvent implements Listener {

    @EventHandler
    public void onCloseEvent(InventoryCloseEvent e) {
        if(!e.getView().getTitle().equals("§8KOTH Rewards")) return;

        if(Main.kothRewardsGUI.contains(e.getPlayer().getUniqueId())) return;
        KOTHItemManager.saveItems(KOTHInventory.saveableItems(e.getView().getTopInventory().getContents()));
        e.getPlayer().sendMessage(Messages.UPDATED_KOTH_REWARDS.queue());
    }
}
