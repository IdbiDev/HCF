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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class Faction_Kick {
    public static Connection con = Main.getConnection("cmd.FactionCreate");
    public static void kick_faction(Player player,String target) {
        if (playertools.getPlayerFaction(player) != null) {
            if (playertools.hasPermission(player, Faction_Rank_Manager.Permissions.MANAGE_KICK)) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                //Player targetPlayer_Online;
                HCFPlayer hcf = HCFPlayer.getPlayer(targetPlayer.getUniqueId());
                Faction f = playertools.getPlayerFaction(targetPlayer);
                if(targetPlayer.getUniqueId() == player.getUniqueId()){
                    player.sendMessage(Messages.cant_kick_yourself.language(player).queue());
                    return;
                }
                if(Objects.equals(targetPlayer.getUniqueId().toString(), f.leader)){
                    player.sendMessage(Messages.cant_kick_leader.language(player).queue());
                    return;
                }

                final String previousRank = hcf.rank.name;
                hcf.removeFaction();

                f.refreshDTR();
                for (Player member : f.getMembers()) {
                    member.sendMessage(Messages.bc_leave_message.language(member).setPlayer(hcf).queue());
                }
                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None","0", hcf.uuid.toString());
                PlayerStatistic stat = hcf.playerStatistic;
                for(FactionHistory statF : stat.factionHistory){
                    if(statF.id == f.id){
                        statF.left = new Date();
                        statF.cause = "Kicked";
                        statF.lastRole = previousRank;
                        statF.name = f.name;
                    }
                }
                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(hcf.name,"kicked",new Date().getTime()));
                Scoreboards.RefreshAll();
                NameChanger.refresh(player);
                player.sendMessage(Messages.kick_message.setExecutor(player).replace("%player%", hcf.name).queue());
            } else {
                player.sendMessage(Messages.no_permission_in_faction.language(player).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.not_in_faction.language(player).queue());
        }

    }
}
