package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class onPlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        String c = HCF_Claiming.sendFactionTerretory(e.getPlayer());
        if(!Objects.equals(playertools.getMetadata(e.getPlayer(), "current_loc"), c)){
            //e.getPlayer().sendMessage(Main.servername+"Kiléptél innen: "+playertools.getMetadata(e.getPlayer(),"current_loc"));
            //e.getPlayer().sendMessage(Main.servername+"Beléptél ide: "+c);
            e.getPlayer().sendMessage(Messages.LEAVE_ZONE.setZone(playertools.getMetadata(e.getPlayer(), "current_loc")).queue());
            e.getPlayer().sendMessage(Messages.ENTERED_ZONE.setZone(c).queue());
            playertools.setMetadata(e.getPlayer(),"current_loc",c);
        }
    }
}
