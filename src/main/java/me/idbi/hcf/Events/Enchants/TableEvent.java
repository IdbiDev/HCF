package me.idbi.hcf.Events.Enchants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class TableEvent implements Listener {

    @EventHandler
    public void prepareEnchantItem(PrepareItemEnchantEvent e) {
        e.setCancelled(true);
        e.getEnchanter().closeInventory();
    }

    @EventHandler
    public void prepareEnchantItem(EnchantItemEvent e) {
        e.setCancelled(true);
        e.getEnchanter().closeInventory();
    }
}