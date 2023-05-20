package me.idbi.hcf.Events;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.Classes.SubClasses.Miner;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Claim;
import me.idbi.hcf.Tools.Objects.ClaimAttributes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SpawnShield;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p); // nincsen tabomxd
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {

            Claim claim = Claiming.sendClaimByXZ(e.getTo().getWorld(),e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
            if (claim != null) {
                if (Claiming.FindPoint_old(claim.getStartX(), claim.getStartZ(), claim.getEndX(), claim.getEndZ(), e.getTo().getBlockX(), e.getTo().getBlockZ()) && ((SpawnShield.pvpCooldown(e.getPlayer()) && claim.getAttribute().equals(ClaimAttributes.NORMAL) || (Timers.COMBAT_TAG.has(p) && claim.getAttribute().equals(ClaimAttributes.PROTECTED))))) {
                    Location loc = new Location(
                            e.getPlayer().getWorld(),
                            e.getFrom().getBlockX(),
                            e.getFrom().getBlockY(),
                            e.getFrom().getBlockZ(),
                            e.getPlayer().getLocation().getYaw(),
                            e.getPlayer().getLocation().getPitch());
                    loc.add(0.5, 0, 0.5);
                    e.getPlayer().teleport(loc);
                    e.getPlayer().sendMessage(Messages.cant_teleport_to_safezone.language(p).queue());
                }
            }
        }
        Playertools.refreshPosition(p);
        if (player.getPlayerClass() == Classes.MINER) {
            Miner.setInvisMode(e.getPlayer(), e.getTo().getY() <= Miner.invisibleLevel);
        }
        if (player.isFreezed()) {
            e.setCancelled(true);
        }/*
        if (HCF_Timer.checkStuckTimer(e.getPlayer())) {
            if (e.getTo().distance(player.stuckLocation) > 4) {
                HCF_Timer.stuckTimers.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(Messages.stuck_finished.language(p).queue());
            }
        }*/
    }
}
