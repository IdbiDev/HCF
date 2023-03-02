package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.idbi.hcf.Tools.Playertools.isTeammate;

public class ProjectileDamage implements Listener {
    Archer archer = new Archer();
    @EventHandler
    public void ProjectileDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile projectile && e.getEntity() instanceof Player victim) {
            if (!(projectile.getShooter() instanceof Player damager)) return;
            Faction damager_faction = Playertools.getPlayerFaction(damager);
            Faction victim_faction = Playertools.getPlayerFaction(victim);

            if (isTeammate(damager, victim)) {
                damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            //Add combatTimer
            if (HCF_Timer.addCombatTimer(victim)) {
                victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
            }
            if (HCF_Timer.addCombatTimer(damager)) {
                damager.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
            }

            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(damager);
            if (!HCF_Timer.checkArcherTimer(victim) && hcfPlayer.playerClass == Classes.ARCHER && archer.archerTagEnabled) {
                HCF_Timer.addArcherTimer(victim);
            }

            if (damager_faction == victim_faction) {
                damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                // damager.sendMessage(Main.servername+ChatColor.RED+"Nem sebezheted a csapattársad!");
                e.setCancelled(true);
            }
        }
    }
}
