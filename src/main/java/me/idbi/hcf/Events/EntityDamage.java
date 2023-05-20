package me.idbi.hcf.Events;

import me.idbi.hcf.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(Main.SOTWEnabled && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
