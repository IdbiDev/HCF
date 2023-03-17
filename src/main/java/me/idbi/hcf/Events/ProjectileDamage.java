package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
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
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);
            /*int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));*/
            if (Timers.PVP_TIMER.has(victimplayer)) {
                damager.sendMessage(Messages.cant_damage_while_pvptimer_victim.language(damager).queue());
                e.setCancelled(true);
                return;
            }
            if (Timers.PVP_TIMER.has(damagerplayer)) {
                damager.sendMessage(Messages.cant_damage_while_pvptimer.language(damager).queue());
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
            //Add combatTimer


            if (!Timers.COMBAT.has(victimplayer)) {
                victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
            }
            if (!Timers.COMBAT.has(damagerplayer)) {
                damager.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
            }

            Timers.COMBAT.add(victimplayer);
            Timers.COMBAT.add(damagerplayer);

            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(damager);
            if (!Timers.ARCHER.has(victimplayer) && hcfPlayer.getPlayerClass() == Classes.ARCHER && archer.archerTagEnabled) {
                Timers.ARCHER.add(victimplayer);
            }
        }
    }
}
