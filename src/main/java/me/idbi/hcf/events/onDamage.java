package me.idbi.hcf.events;


import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class onDamage implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player victim) {
            // Check Friendly Fire
            if (Boolean.parseBoolean(playertools.getMetadata(damager, "adminDuty"))) {
                damager.sendMessage(Messages.CANT_DAMAGE_ADMIN.queue());
                e.setCancelled(true);
            }
            //String c = ChatColor.stripColor(HCF_Claiming.sendFactionTerritory(victim));
            try {
                HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(victim.getLocation().getBlockX(),victim.getLocation().getBlockZ());
                HCF_Claiming.Faction_Claim damagerClaim = HCF_Claiming.sendClaimByXZ(damager.getLocation().getBlockX(), damager.getLocation().getBlockZ());

                if (claim != null) {
                    if(claim.attribute.equals("protected")){
                        e.setCancelled(true);
                        damager.sendMessage(Messages.CANT_DAMAGE_PROTECTED_AREA.queue());
                        return;
                    }
                }
                if (damagerClaim != null) {
                    if(damagerClaim.attribute.equals("protected")){
                        e.setCancelled(true);
                        damager.sendMessage(Messages.CANT_DAMAGE_PROTECTED_AREA.queue());
                    }
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }

        }

    }
}
