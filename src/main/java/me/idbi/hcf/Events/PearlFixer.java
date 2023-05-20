package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Claim;
import me.idbi.hcf.Tools.Objects.ClaimAttributes;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.Point;
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
        HCFRules.setupPearlGlitch();
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
        Scoreboards.refresh(event.getPlayer());
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) && !event.isCancelled() && Timers.COMBAT_TAG.has(event.getPlayer())) {
            Location location = event.getTo();
            for (Map.Entry<Integer, Faction> thisFaction : Main.factionCache.entrySet()) {
                for (Claim claim : thisFaction.getValue().getClaims()) {
                    if(location.getWorld().equals(claim.getWorld())) continue;
                    if (claim.getAttribute().equals(ClaimAttributes.PROTECTED)) {
                        Point claimStart = new Point(claim.getStartX(), claim.getStartZ());
                        Point claimEnd = new Point(claim.getEndX(), claim.getEndZ());
                        Point getTo = new Point(location.getBlockX(), location.getBlockZ());
                        if (Claiming.FindPoint_old(claimStart.getX(), claimStart.getZ(), claimEnd.getX(), claimEnd.getZ(), getTo.getX(), getTo.getZ())) {
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
            if (HCFRules.blockedPearlTypes.contains(to.getBlock().getType())) {
                event.setCancelled(true);
                return;
            }
            to.setX(to.getBlockX() + 0.5D);
            to.setZ(to.getBlockZ() + 0.5D);
            event.setTo(to);
        }
    }
}
