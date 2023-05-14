package me.idbi.hcf.Events;

import me.idbi.hcf.HCFRules;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public static void onEntitySpawn(CreatureSpawnEvent e) {
        if(e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER))
            return;
        //e.getEntityType().name()
        if(HCFRules.getRules().isEntityLimited(e.getEntity(),e.getEntity().getWorld())) {
            e.setCancelled(true);
        }
    }
}
