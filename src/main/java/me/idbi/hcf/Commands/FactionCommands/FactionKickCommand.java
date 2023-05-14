package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Nametag.NameChanger;
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
import java.util.HashMap;
import java.util.Objects;

public class FactionKickCommand extends SubCommand {
    public static Connection con = Main.getConnection();

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
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
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
                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?' WHERE uuid = '?'", "None", "0", hcf.getUUID().toString());
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
                Scoreboards.RefreshAll();
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
