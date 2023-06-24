package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlacePVPTag implements Listener {
    Archer archer = new Archer();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlaceTag(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);

            if(!Playertools.canDmg(victim, damager)) {
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
