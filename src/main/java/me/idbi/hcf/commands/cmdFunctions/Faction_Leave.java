package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
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
import java.util.Objects;

public class Faction_Leave {
    private static final Connection con = Main.getConnection("command.factions");

    public static void leave_faction(Player player) {
        if (playertools.getPlayerFaction(player) != null) {

            Faction f = playertools.getPlayerFaction(player);
            if (!Objects.equals(f.leader, player.getUniqueId().toString())) {

                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None","0", player.getUniqueId().toString());

                player.sendMessage(Messages.leave_message.language(player).queue());

                HCFPlayer hcf = HCFPlayer.getPlayer(player);
                final String rankForLejjebb = hcf.rank.name;

                hcf.removeFaction();

//                displayTeams.removePlayerFromTeam(player);
//                displayTeams.addToNonFaction(player);
                //f.removePrefixPlayer(player);

                for (Player member : f.getMembers()) {
                    member.sendMessage(Messages.bc_leave_message.language(member).setPlayer(player).queue());
                }

                Scoreboards.refresh(player);
                f.refreshDTR();
                PlayerStatistic stat = Main.playerStatistics.get(player.getUniqueId());
                for(FactionHistory statF : stat.factionHistory){
                    if(statF.id == f.id){
                        statF.left = new Date();
                        statF.cause = "Leaved";
                        statF.lastRole = f.player_ranks.get(player).name;
                        statF.name = f.name;
                    }
                }
                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(player.getName(),"leaved",new Date().getTime()));
                Main.playerStatistics.put(player.getUniqueId(),stat);
                NameChanger.refresh(player);
            } else {
                //Todo: Factin leader is a fucking retarded bc he wanna leave the faction. Use /f disband
                player.sendMessage(Messages.leader_leaving_faction.language(player).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.not_in_faction.language(player).queue());
        }
    }
}
