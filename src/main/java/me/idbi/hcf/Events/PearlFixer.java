package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Timers;
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

public class PearlFixer implements Listener {
    //ESKÜ NEM LOPOTT [Source: PearlFixer.java]
    public PearlFixer() {
        HCF_Rules.setupPearlGlitch();
    }

    @EventHandler
    public static void onPearlEvent(PlayerTeleportEvent event) {
        /*if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && !event.isCancelled()) {
            Location location = event.getTo();
            location.setX(location.getBlockX() + 0.5D);
            location.setY(location.getBlockY());
            location.setZ(location.getBlockZ() + 0.5D);
            event.setTo(location);
        }*/
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && !event.isCancelled() && Timers.COMBAT.has(event.getPlayer())) {
            Location location = event.getTo();
            for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
                for (HCF_Claiming.Faction_Claim claim : thisFaction.getValue().getClaims()) {
                    if (claim.getAttribute().equals(HCF_Claiming.ClaimAttributes.PROTECTED)) {
                        HCF_Claiming.Point claimStart = new HCF_Claiming.Point(claim.getStartX(), claim.getStartZ());
                        HCF_Claiming.Point claimEnd = new HCF_Claiming.Point(claim.getEndX(), claim.getEndZ());
                        HCF_Claiming.Point getTo = new HCF_Claiming.Point(location.getBlockX(), location.getBlockZ());
                        if (HCF_Claiming.FindPoint_old(claimStart.getX(), claimStart.getZ(), claimEnd.getX(), claimEnd.getZ(), getTo.getX(), getTo.getZ())) {
                            event.setCancelled(true);
                            event.getPlayer().sendMessage(Messages.cant_teleport_to_safezone.language(event.getPlayer()).queue());
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
