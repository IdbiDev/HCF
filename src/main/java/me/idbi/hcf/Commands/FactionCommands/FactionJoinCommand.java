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

public class FactionJoinCommand extends SubCommand {
    private static final Connection con = Main.getConnection();

    @Override
    public String getName() {
        return "join";
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
        return "/faction join <faction>";
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
        if (Playertools.getPlayerFaction(p) == null) {
            Faction faction = Main.nameToFaction.get(args[1]);
            if (faction == null) {
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
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(), 0L, "", faction.name, faction.getDefaultRank().name, faction.id));
                faction.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(), "invited", new Date().getTime()));
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
