package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.util.Date;
import java.util.Set;

import static me.idbi.hcf.tools.playertools.HasMetaData;

public class onPlayerLeft implements Listener {
    private final Connection con = Main.getConnection("events.Quit");
    private Main m = Main.getPlugin(Main.class);
    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e){
        e.setQuitMessage("");
        // Save deaths,kills money etc
        if(HasMetaData(e.getPlayer(),"faction")){
            SQL_Connection.dbExecute(con,"UPDATE members SET name = '?',money='?',kills='?',deaths='?',online=0 WHERE uuid='?'", e.getPlayer().getName(), playertools.getMetadata(e.getPlayer(),"money"), playertools.getMetadata(e.getPlayer(),"kills"),playertools.getMetadata(e.getPlayer(),"deaths"),e.getPlayer().getUniqueId().toString());
            System.out.println(SQL_Connection.dbExecute(con,"UPDATE members SET online='?' WHERE uuid='?'","0",e.getPlayer().getUniqueId().toString()));
            // Koba moment
            playertools.BroadcastFaction(Main.factionToname.get(Integer.parseInt(playertools.getMetadata(e.getPlayer(),"factionid"))), Messages.LEAVE_FACTION_BC.repPlayer(e.getPlayer()).queue());
            //Main.cacheRanks.remove(e.getPlayer());
        }
        if(Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(),"freeze"))){
            Bukkit.getBanList(BanList.Type.NAME).addBan(e.getPlayer().getName(),
                    m.getConfig().getString("Freeze.Reason"),
                    new Date(System.currentTimeMillis() + m.getConfig().getInt("Freeze.BanTimeSeconds") * 1000L),
                    null)
                    .save();
        }
        Main.player_cache.remove(e.getPlayer());
    }
}
