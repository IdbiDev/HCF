package me.idbi.hcf.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class BowShoot implements Listener {
    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Entity projectile = e.getProjectile();
            //  playertools.setEntityData(projectile, "archertag", true);
        }
    }
}
