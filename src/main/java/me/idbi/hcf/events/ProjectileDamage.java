package me.idbi.hcf.events;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProjectileDamage implements Listener {
    @EventHandler
    public void ProjectileDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile projectile && e.getEntity() instanceof Player victim) {
            if (!(projectile.getShooter() instanceof Player damager)) return;
            int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));

            if (damager_faction == 0 && victim_faction == 0) {
                if (!HCF_Timer.checkArcherTimer(victim) && playertools.getMetadata(damager, "class").equalsIgnoreCase("archer")){
                    HCF_Timer.addArcherTimer(victim);
                }
                return;
            }

            if (damager_faction == victim_faction) {
                damager.sendMessage(Messages.TEAMMATE_DAMAGE.queue());
                // damager.sendMessage(Main.servername+ChatColor.RED+"Nem sebezheted a csapattársad!");
                e.setCancelled(true);
                return;
            }
            if (!HCF_Timer.checkArcherTimer(victim) && playertools.getMetadata(damager, "class").equalsIgnoreCase("archer")) {
                HCF_Timer.addArcherTimer(victim);
            }

        }
    }
}
