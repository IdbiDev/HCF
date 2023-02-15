package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.Classes;
import me.idbi.hcf.classes.subClasses.Miner;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.SpawnShield;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.idbi.hcf.tools.HCF_Timer.checkCombatTimer;

public class onPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        HCFPlayer player = HCFPlayer.getPlayer(p); // nincsen tabomxd
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {

            HCF_Claiming.Faction_Claim claim = HCF_Claiming.sendClaimByXZ(e.getTo().getBlockX(), e.getTo().getBlockZ());
            if (claim != null) {
                if (HCF_Claiming.FindPoint_old(claim.startX, claim.startZ, claim.endX, claim.endZ, e.getTo().getBlockX(), e.getTo().getBlockZ()) && ((SpawnShield.pvpCooldown(e.getPlayer()) && claim.attribute.equals(HCF_Claiming.ClaimAttributes.NORMAL) || (checkCombatTimer(e.getPlayer()) && claim.attribute.equals(HCF_Claiming.ClaimAttributes.PROTECTED))))) {
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

            HCF_Claiming.Faction_Claim c = HCF_Claiming.getPlayerArea(e.getPlayer());
            if (player.currentArea != c) {

                String wilderness = Messages.wilderness.language(p).queue();
                Messages leaveZone = Messages.leave_zone.language(p);
                Messages enteredZone = Messages.entered_zone.language(p);

                Messages enemy = Messages.zone_enemy.language(p);
                Messages friendly = Messages.zone_friendly.language(p);

                e.getPlayer().sendMessage(leaveZone.setZone(player.getLocationFormatted()).queue());

                if(c == null) {
                    e.getPlayer().sendMessage(enteredZone.language(p).setZone(wilderness).queue());
                } else {
                    boolean zoneIsFriendly = c.faction == player.faction;
                    if(!zoneIsFriendly) {
                        zoneIsFriendly = c.faction.isAlly(player.faction);
                    }
                    if(zoneIsFriendly) {
                        e.getPlayer().sendMessage(enteredZone.setZone(friendly.setZone(c.faction.name).queue()).queue());
                    } else {
                        e.getPlayer().sendMessage(enteredZone.setZone(enemy.setZone(c.faction.name).queue()).queue());
                    }
                }
                player.setLocation(c);
                Scoreboards.refresh(e.getPlayer());
            } else {

            }
            if (player.playerClass == Classes.MINER) {
                Miner.setInvisMode(e.getPlayer(), e.getTo().getY() <= Miner.min_y_value);
            }
        }
        if (player.freezeStatus) {
            e.setCancelled(true);
        }
        if (HCF_Timer.checkStuckTimer(e.getPlayer())) {
            if (e.getTo().distance(player.stuckLocation) > 4) {
                HCF_Timer.stuckTimers.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage(Messages.stuck_finished.language(p).queue());
            }
        }

    }
}
