package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static me.idbi.hcf.Tools.Playertools.con;
public class FactionKickCommand extends SubCommand {

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Kicks the selected player from the faction.";
    }

    @Override
    public String getSyntax() {
        return "/faction kick <p>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        kick(p, args[1]);
    }

    public static void kick(Player p, String target) {
        if (Playertools.getPlayerFaction(p) != null) {
            if (Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_KICK)) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                //Player targetPlayer_Online;
                HCFPlayer hcf = HCFPlayer.getPlayer(targetPlayer.getUniqueId());
                Faction f = Playertools.getPlayerFaction(targetPlayer);
                if (targetPlayer.getUniqueId() == p.getUniqueId()) {
                    p.sendMessage(Messages.cant_kick_yourself.language(p).queue());
                    return;
                }
                if (Objects.equals(targetPlayer.getUniqueId().toString(), f.getLeader())) {
                    p.sendMessage(Messages.cant_kick_leader.language(p).queue());
                    return;
                }

                final String previousRank = hcf.getRank().getName();
                hcf.removeFaction();

                f.refreshDTR();
                for (Player member : f.getOnlineMembers()) {
                    member.sendMessage(Messages.member_leave_faction.language(member).setPlayer(hcf).queue());
                }
                if(Main.isUsingMongoDB()) {
                    Bson update = combine(set("faction", 0), set("rank", "None"));
                    MongoDBDriver.Update(MongoDBDriver.MongoCollections.MEMBERS, eq("uuid", hcf.getUUID().toString()), update);
                }else {
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", hcf.getUUID().toString());
                }
                PlayerStatistic stat = hcf.getPlayerStatistic();
                for (FactionHistory statF : stat.factionHistory) {
                    if (statF.id == f.getId()) {
                        statF.left = new Date();
                        statF.cause = "Kicked";
                        statF.lastRole = previousRank;
                        statF.name = f.getName();
                    }
                }

                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(hcf.getName(), "kicked", new Date().getTime()));
                Scoreboards.refreshAll();
                NameChanger.refresh(p);
                p.sendMessage(Messages.kick_message.language(p).setExecutor(p).setPlayer(hcf).queue());
                hcf.sendMessage(Messages.kick_message_target);
            } else {
                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
