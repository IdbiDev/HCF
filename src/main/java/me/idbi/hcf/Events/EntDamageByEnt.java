package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntDamageByEnt implements Listener {
    Archer archer = new Archer();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            // Check Friendly Fire
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);

            if (victimplayer.isInDuty()) {
                damager.sendMessage(Messages.cant_damage_admin.language(damager).queue());
                e.setCancelled(true);
            }
            //String c = ChatColor.stripColor(HCF_Claiming.sendFactionTerritory(victim));
            try {
                Claiming.Faction_Claim claim = victimplayer.getCurrentArea();
                Claiming.Faction_Claim damagerClaim = damagerplayer.getCurrentArea();
                if (claim != null && damagerClaim != null) {
                    if (claim.getAttribute() == Claiming.ClaimAttributes.PROTECTED || damagerClaim.getAttribute() == Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                } else if (claim == null && damagerClaim != null) {
                    if (damagerClaim.getAttribute() == Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                } else if (claim != null) {
                    if (claim.getAttribute() == Claiming.ClaimAttributes.PROTECTED) {
                        e.setCancelled(true);
                        damager.sendMessage(Messages.cant_damage_protected_area.language(damager).queue());
                        return;
                    }
                }

                if(Timers.SOTW.has(damagerplayer)) {
                    damager.sendMessage(Messages.cant_damage_while_pvptimer.language(damager).queue());
                    e.setCancelled(true);
                }
                if(Timers.LOGOUT.has(victimplayer)) {
                    Timers.LOGOUT.remove(victimplayer);
                    victim.sendMessage(Messages.logout_timer_interrupted.language(victim).queue());
                }
                if(Timers.STUCK.has(victimplayer)) {
                    Timers.STUCK.remove(victimplayer);
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
    @EventHandler
    public void onDmg(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(Timers.SOTW.has(p))
                e.setCancelled(true);
        }
    }
}
