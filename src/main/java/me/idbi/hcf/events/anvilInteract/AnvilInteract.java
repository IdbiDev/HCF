package me.idbi.hcf.events.anvilInteract;

import me.idbi.hcf.events.Enchants.EnchantInventory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnvilInteract implements Listener {
    @EventHandler
    public void onTableInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock() != null) {
                if(e.getClickedBlock().getType() == Material.ANVIL) {
                    e.setCancelled(true);
                    e.getPlayer().openInventory(EnchantInventory.inv());
                }
            }
        }
    }
}
