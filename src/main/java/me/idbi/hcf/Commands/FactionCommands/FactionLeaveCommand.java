package me.idbi.hcf.Commands.FactionCommands;


import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static me.idbi.hcf.Tools.Playertools.con;
public class FactionLeaveCommand extends SubCommand {


    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Leaves from faction";
    }

    @Override
    public String getSyntax() {
        return "/faction leave";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {

        if (Playertools.getPlayerFaction(p) != null) {

            Faction f = Playertools.getPlayerFaction(p);
            if (!Objects.equals(f.getLeader(), p.getUniqueId().toString())) {
                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                if(hcf.getCurrentArea().getFaction() == f && !Config.EnableLeaveFriendly.asBoolean()){
                    hcf.sendMessage(Messages.cant_leave_while_on_claim);
                    return;
                }

                if(Main.isUsingMongoDB()) {
                    Bson update = combine(set("faction", 0), set("rank", "None"));
                    MongoDBDriver.Update(MongoDBDriver.MongoCollections.MEMBERS, eq("uuid", hcf.getUUID().toString()), update);
                }else {
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", hcf.getUUID().toString());
                }
                p.sendMessage(Messages.player_leaving_faction_message.language(p).queue());


                final String rankForLejjebb = hcf.getRank().getName();

                hcf.removeFaction();

                for (Player member : f.getOnlineMembers()) {
                    member.sendMessage(Messages.member_leave_faction.language(member).setPlayer(p).queue());
                }

                addCooldown(p);
                Scoreboards.refresh(p);
                hcf.setChatType(ChatTypes.PUBLIC);
                f.refreshDTR();
                PlayerStatistic stat = hcf.getPlayerStatistic();
                for (FactionHistory statF : stat.factionHistory) {
                    if (statF.id == f.getId()) {
                        statF.left = new Date();
                        statF.cause = "Left";
                        statF.lastRole = rankForLejjebb;
                        statF.name = f.getName();
                    }
                }
                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(), "leaved", new Date().getTime()));
                NameChanger.refresh(p);
            } else {
                p.sendMessage(Messages.leader_leaving_faction.language(p).setFaction(f).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
