package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.FactionHistory;
import me.idbi.hcf.tools.Objects.HCFPlayer;
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
        if (playertools.getPlayerFaction(p) == null) {
            Faction faction = Main.nameToFaction.get(factionname);
            if(faction == null){
                Messages.not_found_faction.language(p).queue();
                return;
            }
            if (faction.isPlayerInvited(p)) {

                faction.unInvitePlayer(p);

                //Sikeres belépés
                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                p.sendMessage(Messages.join_message.language(p).queue());
                hcfPlayer.addFaction(faction);
                //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                for (Player member : faction.getMembers()) {
                    member.sendMessage(Messages.bc_join_message.language(member).setPlayer(p).queue());
                }

                SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?' WHERE uuid='?'", String.valueOf(faction.id), faction.getDefaultRank().name, p.getUniqueId().toString());

                // displayTeams.addPlayerToTeam(p);
                //faction.addPrefixPlayer(p);

                Scoreboards.refresh(p);
                faction.refreshDTR();
                PlayerStatistic stat = hcfPlayer.playerStatistic;
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(),0L,"",faction.name, faction.getDefaultRank().name,faction.id));
                faction.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(),"invited",new Date().getTime()));
                //NameChanger.refresh(p);

                NameChanger.refresh(p);


            } else {
                //Nem vagy meghíva ebbe a facionbe
                p.sendMessage(Messages.not_invited.language(p).queue());
            }
        } else {
            // Már vagy egy factionbe
            p.sendMessage(Messages.you_already_in_faction.language(p).queue());
        }
    }
}
