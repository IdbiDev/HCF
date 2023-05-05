package me.idbi.hcf.Events.Enchants;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

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