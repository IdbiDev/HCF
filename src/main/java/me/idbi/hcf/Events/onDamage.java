package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onDamage implements Listener {
    Archer archer = new Archer();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            // Check Friendly Fire
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);

            if (victimplayer.inDuty) {
                damager.sendMessage(Messages.cant_damage_admin.language(damager).queue());
                e.setCancelled(true);
            }
            //String c = ChatColor.stripColor(HCF_Claiming.sendFactionTerritory(victim));
            try {
                HCF_Claiming.Faction_Claim claim = victimplayer.currentArea;
                HCF_Claiming.Faction_Claim damagerClaim = damagerplayer.currentArea;
                if (claim != null && damagerClaim != null) {
                    if (claim.attribute == HCF_Claiming.ClaimAttributes.PROTECTED || damagerClaim.attribute == HCF_Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                } else if (claim == null && damagerClaim != null) {
                    if (damagerClaim.attribute == HCF_Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                } else if (claim != null) {
                    if (claim.attribute == HCF_Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                }
                if(HCF_Timer.getLogoutTime(victim) != 0) {
                    HCF_Timer.removeLogoutTimer(victim);
                    victim.sendMessage(Messages.logout_timer_interrupted.language(victim).queue());
                }
                if(HCF_Timer.getStuckTime(victim) != 0) {
                    HCF_Timer.removeLogoutTimer(victim);
                    victim.sendMessage(Messages.stuck_interrupted.language(victim).queue());
                }
                /*if (damagerClaim != null) {
                    if(damagerClaim.attribute.equals("protected")){
                        e.setCancelled(true);
                        damager.sendMessage(Messages.CANT_DAMAGE_PROTECTED_AREA.queue());
                        return;
                    }
                }*/

                /*if (isTeammate(damager, victim)) {
                    damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                    e.setCancelled(true);
                    return;
                }*/


                //Add combatTimer
                /*if (HCF_Timer.addCombatTimer(victim)) {
                    victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
                }
                if (HCF_Timer.addCombatTimer(damager)) {
                    damager.sendMessage(Messages.combat_message.language(damager).queue().replace("%sec%", Config.CombatTag.asStr()));
                }
                //Damage if ArcherTag
                if (HCF_Timer.checkArcherTimer(victim)) {
                    double dmg = e.getDamage();
                    e.setDamage(dmg + (dmg * archer.archerTagDamageAmplifier / 100)); // nem static
                }*/
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

        }

    }
}
