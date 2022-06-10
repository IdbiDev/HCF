package me.idbi.hcf.events;

import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class onBowShoot implements Listener {
    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Entity projectile = e.getProjectile();
            playertools.setEntityData(projectile, "archertag", true);
        }
    }
}
