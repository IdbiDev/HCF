package me.idbi.hcf.events;

import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;

import static me.idbi.hcf.tools.HCF_Timer.checkCombatTimer;

public class PearlFixer implements Listener {
    //ESKÜ NEM LOPOTT [Source: PearlFixer.java]
    public PearlFixer(){
        HCF_Rules.setupPearlGlitch();
    }
    @EventHandler
    public static void onPearlEvent(PlayerTeleportEvent event){
        /*if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && !event.isCancelled()) {
            Location location = event.getTo();
            location.setX(location.getBlockX() + 0.5D);
            location.setY(location.getBlockY());
            location.setZ(location.getBlockZ() + 0.5D);
            event.setTo(location);
        }*/
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && !event.isCancelled() && checkCombatTimer(event.getPlayer())) {
            Location location = event.getTo();
            for (Map.Entry<Integer, Main.Faction> thisFaction : Main.faction_cache.entrySet()) {
                for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().claims) {
                    if(claim.attribute.equalsIgnoreCase("protected")){
                        HCF_Claiming.Point claimStart = new HCF_Claiming.Point(claim.startX, claim.startZ);
                        HCF_Claiming.Point claimEnd = new HCF_Claiming.Point(claim.endX, claim.endZ);
                        HCF_Claiming.Point getTo = new HCF_Claiming.Point(location.getBlockX(), location.getBlockZ());
                        if(HCF_Claiming.FindPoint_old(claimStart.x,claimStart.z,claimEnd.x,claimEnd.z,getTo.x,getTo.z)){
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(Messages.CANT_TELEPORT_TO_SAFEZONE.queue());
                            break;
                        }
                    }
                }
            }

        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() &&
                event.getItem().getType() == Material.ENDER_PEARL) {
            Block block = event.getClickedBlock();
            if (block.getType().isSolid() && !(block.getState() instanceof org.bukkit.inventory.InventoryHolder)) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                player.setItemInHand(event.getItem());
            }
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPearlClip(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();
            if (HCF_Rules.blockedPearlTypes.contains(to.getBlock().getType())) {
                event.setCancelled(true);
                return;
            }
            to.setX(to.getBlockX() + 0.5D);
            to.setZ(to.getBlockZ() + 0.5D);
            event.setTo(to);
        }
    }
}
