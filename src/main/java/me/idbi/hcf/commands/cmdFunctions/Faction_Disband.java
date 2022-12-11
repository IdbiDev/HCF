package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Discord.LogLibrary;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class Faction_Disband {
    private static final Connection con = Main.getConnection("faction");

    public static void disband(Player p, String faction) {
        if (!Objects.equals(playertools.getMetadata(p, "faction"), faction)) {
            p.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            GUI_Sound.playSound(p,"error");
            return;
        }

        Main.Faction selectedFaction = Main.nameToFaction.get(playertools.getMetadata(p, "faction"));
        if (!Objects.equals(selectedFaction.leader, p.getUniqueId().toString())) {
            //Todo: Nope factionLeader
            p.sendMessage(Messages.NOT_LEADER.queue());
            GUI_Sound.playSound(p,"error");
            return;
        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);

        Main.faction_cache.remove(selectedFaction.id);
        Main.nameToFaction.remove(selectedFaction.name);
        Main.factionToname.remove(selectedFaction.id);
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.id));
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='none',faction=0,factionname='None' WHERE faction='?'", String.valueOf(selectedFaction.id));
        for (Player player : playertools.getFactionOnlineMembers(selectedFaction)) {
            playertools.setMetadata(player, "faction", "None");
            playertools.setMetadata(player, "factionid", "0");
            playertools.setMetadata(player, "rank", "none");
            Scoreboards.refresh(player);
            Main.PlayerStatistic stat = Main.playerStatistics.get(player);
            for(Main.FactionHistory f : stat.factionHistory){
                if(f.id == selectedFaction.id){
                    f.left = new Date();
                    f.cause = "Disbanded";
                    f.lastRole = selectedFaction.player_ranks.get(player).name;
                    f.name = selectedFaction.name;
                }
            }
            Main.playerStatistics.put(player,stat);
        }
        LogLibrary.sendFactionDisband(p, selectedFaction.name);
        Bukkit.broadcastMessage(Messages.FACTION_DISBAND
                .repPlayer(p)
                .setFaction(selectedFaction.name)
                .queue());
        Scoreboards.refresh(p);
        GUI_Sound.playSound(p,"success");
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
