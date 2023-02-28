package me.idbi.hcf.Commands.FactionCommands;


import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class FactionLeaveCommand extends SubCommand {
    private static final Connection con = Main.getConnection("command.factions");

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
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public void perform(Player p, String[] args) {

        if (Playertools.getPlayerFaction(p) != null) {

            Faction f = Playertools.getPlayerFaction(p);
            if (!Objects.equals(f.leader, p.getUniqueId().toString())) {

                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", p.getUniqueId().toString());

                p.sendMessage(Messages.leave_message.language(p).queue());

                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                final String rankForLejjebb = hcf.rank.name;

                hcf.removeFaction();

//                displayTeams.removePlayerFromTeam(p);
//                displayTeams.addToNonFaction(p);
                //f.removePrefixPlayer(p);

                for (Player member : f.getMembers()) {
                    member.sendMessage(Messages.bc_leave_message.language(member).setPlayer(p).queue());
                }

                Scoreboards.refresh(p);
                f.refreshDTR();
                PlayerStatistic stat = hcf.playerStatistic;
                for (FactionHistory statF : stat.factionHistory) {
                    if (statF.id == f.id) {
                        statF.left = new Date();
                        statF.cause = "Left";
                        statF.lastRole = rankForLejjebb;
                        statF.name = f.name;
                    }
                }
                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(), "leaved", new Date().getTime()));
                NameChanger.refresh(p);
            } else {
                //Todo: Faction leader is a fucking retarded bc he wanna leave the faction. Use /f disband
                p.sendMessage(Messages.leader_leaving_faction.language(p).setFaction(f).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
