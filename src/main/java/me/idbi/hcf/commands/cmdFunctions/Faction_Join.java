package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.FactionHistory;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

public class Faction_Join {

    private static final Connection con = Main.getConnection("commands.Faction");

    public static void JoinToFaction(Player p, String factionname) {
        if (playertools.getMetadata(p, "factionid").equals("0")) {
            Integer id_faction = 0;
            for (Map.Entry<Integer, String> entry : Main.factionToname.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(factionname)) {
                    id_faction = entry.getKey();
                    break;
                }
            }
            Faction faction = Main.faction_cache.get(id_faction);
            if(faction == null) return;
            if (faction.isPlayerInvited(p)) {

                faction.unInvitePlayer(p);

                //Sikeres belépés
                p.sendMessage(Messages.JOIN_MESSAGE.queue());
                playertools.setMetadata(p, "factionid", id_faction);
                playertools.setMetadata(p, "faction", faction.name);
                Faction_Rank_Manager.Rank defa = faction.getDefaultRank();
                faction.ApplyPlayerRank(p, defa.name);
                //Faction -> xy belépett
                faction.memberCount++;
                //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                faction.BroadcastFaction(Messages.BC_JOIN_MESSAGE.repPlayer(p).queue());

                SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',factionname='?',rank='?' WHERE uuid='?'", String.valueOf(id_faction), faction.name, faction.getDefaultRank().name, p.getUniqueId().toString());

                // displayTeams.addPlayerToTeam(p);
                //faction.addPrefixPlayer(p);

                Scoreboards.refresh(p);
                faction.refreshDTR();
                PlayerStatistic stat = Main.playerStatistics.get(p);
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(),0L,"",faction.name, defa.name,faction.id));
                Main.playerStatistics.put(p,stat);
                faction.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(),"invited",new Date().getTime()));
                //NameChanger.refresh(p);

                NameChanger.refresh(p);


            } else {
                //Nem vagy meghíva ebbe a facionbe
                p.sendMessage(Messages.NOT_INVITED.queue());
            }
        } else {
            // Már vagy egy factionbe
            p.sendMessage(Messages.YOU_ALREADY_IN_FACTION.queue());
        }
    }
}
