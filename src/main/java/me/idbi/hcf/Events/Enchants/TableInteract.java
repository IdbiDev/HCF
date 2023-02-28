package me.idbi.hcf.Events.Enchants;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TableInteract implements Listener {

    @EventHandler
    public void onTableInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
                    e.setCancelled(true);
                    e.getPlayer().openInventory(EnchantInventory.inv());
                }
            }
        }
    }
}
