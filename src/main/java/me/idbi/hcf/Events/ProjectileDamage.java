package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Archer;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class ProjectileDamage implements Listener {
    Archer archer = new Archer();
    @EventHandler
    public void ProjectileDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile && e.getEntity() instanceof Player) {
            Projectile projectile = (Projectile) e.getDamager();
            Player victim = (Player) e.getEntity();
            if (!(projectile.getShooter() instanceof Player)) return;
            Player damager = (Player) projectile.getShooter();
            Faction damager_faction = Playertools.getPlayerFaction(damager);
            Faction victim_faction = Playertools.getPlayerFaction(victim);
            HCFPlayer victimplayer = HCFPlayer.getPlayer(victim);
            HCFPlayer damagerplayer = HCFPlayer.getPlayer(damager);
            /*int damager_faction = Integer.parseInt(playertools.getMetadata(damager, "factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim, "factionid"));*/
            if(!Playertools.canDmg(victim, damager))
                return;
            //Add combatTimer

            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                if (!Timers.COMBAT_TAG.has(victimplayer)) {
                    victim.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
                }
                if (!Timers.COMBAT_TAG.has(damagerplayer)) {
                    damager.sendMessage(Messages.combat_message.language(victim).queue().replace("%sec%", Config.CombatTag.asStr()));
                }

                Timers.COMBAT_TAG.add(victimplayer);
                Timers.COMBAT_TAG.add(damagerplayer);
            }

            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(damager);
            if (Timers.ARCHER_TAG.has(victimplayer)) {
                double dmg = e.getDamage();
                e.setDamage(dmg + (dmg * archer.archerTagDamageAmplifier / 100));
                System.out.println(dmg + (dmg * archer.archerTagDamageAmplifier / 100));
            }
            if (!Timers.ARCHER_TAG.has(victimplayer) && hcfPlayer.getPlayerClass() == Classes.ARCHER && archer.archerTagEnabled) {
                Timers.ARCHER_TAG.add(victimplayer);
                NameChanger.refresh(victim);
                System.out.println("Archer tag added");
            }

        }
    }
}
