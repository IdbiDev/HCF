package me.idbi.hcf.events;

import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


import java.util.Objects;

public class onPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if(Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(),"freeze"))){
            e.setCancelled(true);
        }
        String c = HCF_Claiming.sendFactionTerretory(e.getPlayer());
        if(!Objects.equals(playertools.getMetadata(e.getPlayer(), "current_loc"), c)){
            e.getPlayer().sendMessage(Messages.LEAVE_ZONE.setZone(playertools.getMetadata(e.getPlayer(), "current_loc")).queue());
            e.getPlayer().sendMessage(Messages.ENTERED_ZONE.setZone(c).queue());
            playertools.setMetadata(e.getPlayer(),"current_loc",c);
            Scoreboards.refresh(e.getPlayer());
        }
    }
}
