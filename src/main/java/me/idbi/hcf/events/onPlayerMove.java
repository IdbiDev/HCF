package me.idbi.hcf.events;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.classes.Miner;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

import static me.idbi.hcf.tools.playertools.getRealMetadata;

public class onPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "freeze"))) {
            e.setCancelled(true);
        }
        if(HCF_Timer.checkStuckTimer(e.getPlayer())){
            if(e.getTo().distance((Location) getRealMetadata(e.getPlayer(),"stuck_loc")) > 4){
                    HCF_Timer.stuckTimers.remove(e.getPlayer());
                    e.getPlayer().sendMessage(Messages.STUCK_INTERRUPTED.queue());
            }
        }
        String c = HCF_Claiming.sendFactionTerritory(e.getPlayer());
        if (!Objects.equals(playertools.getMetadata(e.getPlayer(), "current_loc"), c)) {
            e.getPlayer().sendMessage(Messages.LEAVE_ZONE.setZone(playertools.getMetadata(e.getPlayer(), "current_loc")).queue());
            e.getPlayer().sendMessage(Messages.ENTERED_ZONE.setZone(c).queue());
            playertools.setMetadata(e.getPlayer(), "current_loc", c);
            Scoreboards.refresh(e.getPlayer());
        }
        if (playertools.getMetadata(e.getPlayer(), "class").equalsIgnoreCase("miner")) {
            Miner.setInvisMode(e.getPlayer(), e.getTo().getY() <= Miner.min_y_value);
        }

        if(e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            //SpawnShield.CalcWall(e.getPlayer());
        }
    }
}
