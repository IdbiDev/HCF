package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.displayTeams;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;
import java.util.Objects;

public class onPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playertools.LoadPlayer(p);
        //Koba moment
        e.setJoinMessage("");
        if(!Objects.equals(playertools.getMetadata(p, "factionid"), "0")) {
            Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
            f.BroadcastFaction(
                    Messages.JOIN_FACTION_BC.repPlayer(p).queue());
        }

//        displayTeams.setupPlayer(e.getPlayer());
//        displayTeams.addPlayerToTeam(e.getPlayer());
        Scoreboards.refresh(p);

        for(Map.Entry<LivingEntity, Long> entity : Main.saved_players.entrySet()) {
            if(Bukkit.getPlayer(entity.getKey().getCustomName()) != null) {
                if(Bukkit.getPlayer(entity.getKey().getCustomName()).isOnline()) {
                    entity.getKey().remove();

                    Main.saved_players.remove(entity.getKey());
                    Main.saved_items.remove(entity.getKey());
                }
            }
        }
    }
}
