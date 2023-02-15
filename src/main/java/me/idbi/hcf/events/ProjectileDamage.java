package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.idbi.hcf.tools.playertools.isTeammate;

public class ProjectileDamage implements Listener {
    @EventHandler
    public void ProjectileDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile projectile && e.getEntity() instanceof Player victim) {
            if (!(projectile.getShooter() instanceof Player damager)) return;
            Faction damager_faction = playertools.getPlayerFaction(damager);
            Faction victim_faction = playertools.getPlayerFaction(victim);

            if (isTeammate(damager,victim)) {
                damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            //Add combatTimer
            if (HCF_Timer.addCombatTimer(victim)) {
                victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.combattag.asStr()));
            }
            if (HCF_Timer.addCombatTimer(damager)) {
                damager.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.combattag.asStr()));
            }
            if (damager_faction == 0 && victim_faction == 0) {
                if (!HCF_Timer.checkArcherTimer(victim) && playertools.getMetadata(damager, "class").equalsIgnoreCase("archer")){
                    HCF_Timer.addArcherTimer(victim);
                }
                return;
            }

            if (damager_faction == victim_faction) {
                damager.sendMessage(Messages.teammate_damage.language(damager).queue());
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
