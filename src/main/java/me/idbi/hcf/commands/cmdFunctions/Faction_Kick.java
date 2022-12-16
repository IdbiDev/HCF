package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class Faction_Kick {
    public static Connection con = Main.getConnection("cmd.FactionCreate");
    public static void kick_faction(Player player,String target) {
        if (!playertools.getMetadata(player, "factionid").equals("0")) {
            if (playertools.hasPermission(player, Faction_Rank_Manager.Permissions.MANAGE_KICK)) {
                OfflinePlayer targetPlayer_Offline = Bukkit.getOfflinePlayer(target);
                Player targetPlayer_Online;

                //Todo: Kick message
                if(targetPlayer_Offline.isOnline()) {
                    targetPlayer_Online = targetPlayer_Offline.getPlayer();
                    if(targetPlayer_Online == player){
                        player.sendMessage(Messages.CANT_KICK_YOURSELF.queue());
                        return;
                    }

                    Main.Faction f = playertools.getPlayerFaction(targetPlayer_Online);
                    assert f != null;
                    if(Objects.equals(targetPlayer_Online.getUniqueId().toString(), f.leader)){
                        player.sendMessage(Messages.CANT_KICK_LEADER.queue());
                        return;
                    }
                    playertools.setMetadata(targetPlayer_Online, "faction", "None");
                    playertools.setMetadata(targetPlayer_Online, "factionid", "0");
                    playertools.setMetadata(targetPlayer_Online, "rank", "None");
                    f.player_ranks.remove(targetPlayer_Online);
                    f.memberCount--;
                    f.BroadcastFaction(Messages.BC_LEAVE_MESSAGE.repPlayer(targetPlayer_Online).queue());
                    f.refreshDTR();
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?',factionname='?' WHERE uuid = '?'", "None","0", "None", targetPlayer_Online.getUniqueId().toString());

//                    displayTeams.removePlayerFromTeam(targetPlayer_Online);
//                    displayTeams.addToNonFaction(targetPlayer_Online);
                    f.removePrefixPlayer(targetPlayer_Online);
                    Main.PlayerStatistic stat = Main.playerStatistics.get(targetPlayer_Online);
                    for(Main.FactionHistory statF : stat.factionHistory){
                        if(statF.id == f.id){
                            statF.left = new Date();
                            statF.cause = "Kicked";
                            statF.lastRole = f.player_ranks.get(player).name;
                            statF.name = f.name;
                        }
                    }
                    Main.playerStatistics.put(targetPlayer_Online,stat);
                    f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(targetPlayer_Online.getName(),"kicked",new Date().getTime()));

                    Scoreboards.RefreshAll();
                } else {
                    Main.Faction f = playertools.getPlayerFaction(player);
                    assert f != null;
                    if(Objects.equals(targetPlayer_Offline.getUniqueId().toString(), f.leader)){
                        player.sendMessage(Messages.CANT_KICK_LEADER.queue());
                        return;
                    }
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?',factionname='?' WHERE uuid = '?'", "None","0", "None", targetPlayer_Offline.getUniqueId().toString());
                    f.memberCount--;
                    f.refreshDTR();
                    Scoreboards.RefreshAll();
                    f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(targetPlayer_Offline.getName(),"kicked",new Date().getTime()));
                }
                player.sendMessage(Messages.MAX_MEMBERS_REACHED.queue());
            } else {
                player.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.NOT_IN_FACTION.queue());
        }

    }
}
