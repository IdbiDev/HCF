package me.idbi.hcf.Enchants;

import me.idbi.hcf.Main;
import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import java.util.Arrays;

public class TableEvent implements Listener {

    @EventHandler
    public void prepareEnchantItem(PrepareItemEnchantEvent e){
        e.setCancelled(true);
        e.getEnchanter().closeInventory();
    }
}