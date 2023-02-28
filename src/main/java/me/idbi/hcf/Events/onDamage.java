package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.idbi.hcf.Tools.Playertools.isTeammate;

public class onDamage implements Listener {

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
                HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(victim.getLocation().getBlockX(), victim.getLocation().getBlockZ());
                HCF_Claiming.Faction_Claim damagerClaim = HCF_Claiming.sendClaimByXZ(damager.getLocation().getBlockX(), damager.getLocation().getBlockZ());
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
                /*if (damagerClaim != null) {
                    if(damagerClaim.attribute.equals("protected")){
                        e.setCancelled(true);
                        damager.sendMessage(Messages.CANT_DAMAGE_PROTECTED_AREA.queue());
                        return;
                    }
                }*/
                if (HCF_Timer.getPvPTimerCoolDownSpawn(victim) != 0) {
                    damager.sendMessage(Messages.cant_damage_while_pvptimer_victim.language(damager).queue());
                    e.setCancelled(true);
                }
                if (HCF_Timer.getPvPTimerCoolDownSpawn(damager) != 0) {
                    damager.sendMessage(Messages.cant_damage_while_pvptimer.language(damager).queue());
                    e.setCancelled(true);
                }
                if (isTeammate(damager, victim)) {
                    damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                    e.setCancelled(true);
                    return;
                }
                if (damagerplayer.faction != null && victimplayer.faction != null) {
                    Faction vicFac = victimplayer.faction;
                    Faction damFac = damagerplayer.faction;
                    if (damFac.isAlly(vicFac)) {
                        if (!vicFac.HaveAllyPermission(damFac, Permissions.FRIENDLY_FIRE) || !damFac.HaveAllyPermission(vicFac, Permissions.FRIENDLY_FIRE)) {
                            damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                            e.setCancelled(true);
                            return;
                        }
                    }
                }

                //Add combatTimer
                if (HCF_Timer.addCombatTimer(victim)) {
                    victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.combattag.asStr()));
                }
                if (HCF_Timer.addCombatTimer(damager)) {
                    damager.sendMessage(Messages.combat_message.language(damager).queue().replace("%sec%", Config.combattag.asStr()));
                }
                //Damage if ArcherTag
                if (HCF_Timer.checkArcherTimer(victim)) {
                    double dmg = e.getDamage();
                    e.setDamage(dmg + (dmg * Archer.ArcherTagDamageAmplifier / 100));
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

        }

    }
}
