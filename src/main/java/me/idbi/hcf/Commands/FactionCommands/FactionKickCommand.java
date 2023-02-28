package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.Objects;

public class FactionKickCommand extends SubCommand {
    public static Connection con = Main.getConnection("cmd.FactionCreate");

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
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction kick <p>";
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
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
                if (Objects.equals(targetPlayer.getUniqueId().toString(), f.leader)) {
                    p.sendMessage(Messages.cant_kick_leader.language(p).queue());
                    return;
                }

                final String previousRank = hcf.rank.name;
                hcf.removeFaction();

                f.refreshDTR();
                for (Player member : f.getMembers()) {
                    member.sendMessage(Messages.bc_leave_message.language(member).setPlayer(hcf).queue());
                }
                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", hcf.uuid.toString());
                PlayerStatistic stat = hcf.playerStatistic;
                for (FactionHistory statF : stat.factionHistory) {
                    if (statF.id == f.id) {
                        statF.left = new Date();
                        statF.cause = "Kicked";
                        statF.lastRole = previousRank;
                        statF.name = f.name;
                    }
                }
                f.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(hcf.name, "kicked", new Date().getTime()));
                Scoreboards.RefreshAll();
                NameChanger.refresh(p);
                p.sendMessage(Messages.kick_message.setExecutor(p).replace("%p%", hcf.name).queue());
            } else {
                p.sendMessage(Messages.no_permission_in_faction.language(p).queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}