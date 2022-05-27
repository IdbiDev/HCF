package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class onPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playertools.LoadPlayer(p);
        //Koba moment
        e.setJoinMessage("");
        if(!Objects.equals(playertools.getMetadata(p, "factionid"), "0")) {
            playertools.BroadcastFaction(Main.factionToname.get(Integer.valueOf(playertools.getMetadata(p,"factionid"))),
                    Messages.JOIN_FACTION_BC.repPlayer(p).queue());
        }
        Scoreboards.refresh(p);

    }
}
