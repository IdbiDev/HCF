package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static me.idbi.hcf.Tools.Playertools.isTeammate;

public class PlacePVPTag implements Listener {
Archer archer = new Archer();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlaceTag(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);
            /*int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));*/
            if (Timers.PVP_TIMER.has(victimplayer) || Timers.PVP_TIMER.has(damagerplayer)) {
                damager.sendMessage(Messages.cant_damage_while_pvptimer_victim.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            if (Timers.SOTW.has(damagerplayer) || Timers.SOTW.has(victimplayer) ) {
                damager.sendMessage(Messages.cant_damage_while_sotw_timer_active.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            if (damagerplayer.getFaction() != null && victimplayer.getFaction() != null) {
                Faction vicFac = victimplayer.getFaction();
                Faction damFac = damagerplayer.getFaction();
                if (damFac.isAlly(vicFac)) {
                    if (!vicFac.hasAllyPermission(damFac, Permissions.FRIENDLY_FIRE) || !damFac.hasAllyPermission(vicFac, Permissions.FRIENDLY_FIRE)) {
                        damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                        e.setCancelled(true);
                        return;
                    }
                }
            }
            if (isTeammate(damager, victim) && damager != victim) {
                damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                //Add combatTimer
                if (!Timers.COMBAT_TAG.has(victimplayer)) {
                    victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
                }
                if (!Timers.COMBAT_TAG.has(damagerplayer)) {
                    damager.sendMessage(Messages.combat_message.language(damager).queue().replace("%sec%", Config.CombatTag.asStr()));
                }
                Timers.COMBAT_TAG.add(victimplayer);
                Timers.COMBAT_TAG.add(damagerplayer);
            }
            //Damage if ArcherTag
            if (Timers.ARCHER_TAG.has(victimplayer)) {
                double dmg = e.getDamage();
                e.setDamage(dmg + (dmg * archer.archerTagDamageAmplifier / 100));
            }

        }
    }
}
