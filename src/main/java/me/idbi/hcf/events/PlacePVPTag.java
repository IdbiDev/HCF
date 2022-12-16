package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.subClasses.Archer;
import me.idbi.hcf.tools.HCF_Timer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.idbi.hcf.tools.playertools.isTeammate;

public class PlacePVPTag implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void PlaceTag(EntityDamageByEntityEvent e){
        if(e.isCancelled()) return;
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            /*int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));*/


            if (isTeammate(damager,victim)) {
                damager.sendMessage(Messages.TEAMMATE_DAMAGE.queue());
                e.setCancelled(true);
                return;
            }
            //Add combatTimer
            if (HCF_Timer.addCombatTimer(victim)) {
                victim.sendMessage(Messages.COMBAT_MESSAGE.queue().replace("%sec%", ConfigLibrary.Combat_time.getValue()));
            }
            if (HCF_Timer.addCombatTimer(damager)) {
                damager.sendMessage(Messages.COMBAT_MESSAGE.queue().replace("%sec%", ConfigLibrary.Combat_time.getValue()));
            }
            //Damage if ArcherTag
            if (HCF_Timer.checkArcherTimer(victim)) {
                double dmg = e.getDamage();
                e.setDamage(dmg + (dmg * Archer.ArcherTagDamageAmplifier / 100));
            }

        }
    }
}
