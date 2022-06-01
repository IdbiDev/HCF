package me.idbi.hcf.events;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.Archer;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class onDamage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {


        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            ItemStack damagerWeapon = ((Player) e.getDamager()).getItemInHand();
            // Check Friendly Fire
            if(Boolean.parseBoolean(playertools.getMetadata(damager,"adminDuty"))){
                damager.sendMessage(Messages.CANT_DAMAGE_ADMIN.queue());
                e.setCancelled(true);
            }
            int damager_faction = Integer.parseInt(playertools.getMetadata(damager,"factionid"));
            int victim_faction = Integer.parseInt(playertools.getMetadata(victim,"factionid"));

            if(damager_faction == 0 && victim_faction == 0){
                if(HCF_Timer.addCombatTimer(victim)){
                    victim.sendMessage(Messages.COMBAT_MESSAGE.queue());
                }
                if(HCF_Timer.addCombatTimer(damager)){
                    damager.sendMessage(Messages.COMBAT_MESSAGE.queue());
                }
                return;
            }


            if (damager_faction == victim_faction){
                damager.sendMessage(Messages.TEAMMATE_DAMAGE.queue());
                e.setCancelled(true);

                return;
            }
            //Add combatTimer
            if(HCF_Timer.addCombatTimer(victim)){
                victim.sendMessage(Messages.COMBAT_MESSAGE.queue());
            }
            if(HCF_Timer.addCombatTimer(damager)){
                damager.sendMessage(Messages.COMBAT_MESSAGE.queue());
            }
            //Damage if ArcherTag
            if (HCF_Timer.checkArcherTimer(victim)){
                double dmg = e.getDamage();
                System.out.println("Archertag damage");
                e.setDamage(dmg+(dmg* Archer.ArcherTagModifyer/100));
            }

        }
        if (e.getDamager() instanceof Projectile && e.getEntity() instanceof Player){
            Projectile projectile = (Projectile) e.getDamager();
            Player damager = (Player) projectile.getShooter();
            if (damager != null) {
                Player victim = (Player) e.getEntity();
                int damager_faction = Integer.parseInt(playertools.getMetadata(damager,"factionid"));
                int victim_faction = Integer.parseInt(playertools.getMetadata(victim,"factionid"));

                if(damager_faction == 0 && victim_faction == 0) {
                    if (!HCF_Timer.checkArcherTimer(victim) && playertools.getMetadata(damager, "class").equals("archer"))
                        System.out.println("Archertag added");
                    HCF_Timer.addArcherTimer(victim);
                    return;
                }

                if (damager_faction == victim_faction){
                    damager.sendMessage(Messages.TEAMMATE_DAMAGE.queue());
                   // damager.sendMessage(Main.servername+ChatColor.RED+"Nem sebezheted a csapattársad!");
                    e.setCancelled(true);
                    return;
                }
                if(!HCF_Timer.checkArcherTimer(victim) && playertools.getMetadata(damager,"class").equals("archer"))
                    System.out.println("Archertag added");
                HCF_Timer.addArcherTimer(victim);
            }

        }
    }
}
