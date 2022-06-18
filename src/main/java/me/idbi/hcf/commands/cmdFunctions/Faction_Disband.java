package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Discord.LogLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Objects;

public class Faction_Disband {
    private static final Connection con = Main.getConnection("faction");

    public static void disband(Player p, String faction) {
        if (!Objects.equals(playertools.getMetadata(p, "faction"), faction)) {
            p.sendMessage(Messages.NOT_FOUND_FACTION.queue());
            return;
        }

        Main.Faction selectedFaction = Main.nameToFaction.get(playertools.getMetadata(p, "faction"));
        if (!Objects.equals(selectedFaction.leader, p.getUniqueId().toString())) {
            //Todo: Nope factionLeader
            p.sendMessage(Messages.NOT_LEADER.queue());
            return;
        }
//        displayTeams.removePlayerFromTeam(p);
//        displayTeams.removeTeam(selectedFaction);

        Main.faction_cache.remove(selectedFaction.factionid);
        Main.nameToFaction.remove(selectedFaction.factioname);
        Main.factionToname.remove(selectedFaction.factionid);
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE faction='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con, "DELETE FROM factions WHERE ID='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con, "DELETE FROM claims WHERE factionid='?'", String.valueOf(selectedFaction.factionid));
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='Nincs',faction=0,factionname='Nincs' WHERE faction='?'", String.valueOf(selectedFaction.factionid));
        for (Player player : playertools.getFactionOnlineMembers(faction)) {
            playertools.setMetadata(player, "faction", "Nincs");
            playertools.setMetadata(player, "factionid", "0");
            playertools.setMetadata(player, "rank", "none");
            Scoreboards.refresh(player);
        }
        LogLibrary.sendFactionDisband(p, selectedFaction.factioname);
        Bukkit.broadcastMessage(Messages.FACTION_DISBAND
                .repPlayer(p)
                .setFaction(selectedFaction.factioname)
                .queue());
        Scoreboards.refresh(p);
        //Bukkit.broadcastMessage(Messages.DELETE_FACTION_BY_ADMIN.repExecutor(p).setFaction(selectedFaction.factioname).queue());
    }
}
