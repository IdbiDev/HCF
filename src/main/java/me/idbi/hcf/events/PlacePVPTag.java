package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.Archer;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlacePVPTag implements Listener {

    @EventHandler
    public void PlaceTag(EntityDamageByEntityEvent e){
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));

            if (damager_faction == victim_faction) {
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
                System.out.println("Archertag damage");
                e.setDamage(dmg + (dmg * Archer.ArcherTagModifyer / 100));
            }
            if (damager_faction == 0 && victim_faction == 0) {
                if (HCF_Timer.addCombatTimer(victim)) {
                    victim.sendMessage(Messages.COMBAT_MESSAGE.queue().replace("%sec%", ConfigLibrary.Combat_time.getValue()));
                }
                if (HCF_Timer.addCombatTimer(damager)) {
                    damager.sendMessage(Messages.COMBAT_MESSAGE.queue().replace("%sec%", ConfigLibrary.Combat_time.getValue()));
                }
            }
        }
    }
}