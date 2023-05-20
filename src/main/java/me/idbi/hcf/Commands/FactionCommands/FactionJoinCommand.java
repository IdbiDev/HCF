package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

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
        return "Joins the faction to which you have already been invited.";
    }

    @Override
    public String getSyntax() {
        return "/faction join <faction>";
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
                p.sendMessage(Messages.player_joined_faction_message.language(p).queue());
                hcfPlayer.addFaction(faction);
                //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                for (Player member : faction.getOnlineMembers()) {
                    member.sendMessage(Messages.new_member_join_faction.language(member).setPlayer(p).queue());
                }

                SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?' WHERE uuid='?'", String.valueOf(faction.getId()), faction.getDefaultRank().getName(), p.getUniqueId().toString());

                // displayTeams.addPlayerToTeam(p);
                //faction.addPrefixPlayer(p);

                addCooldown(p);
                Scoreboards.refresh(p);
                hcfPlayer.setChatType(ChatTypes.PUBLIC);
                faction.refreshDTR();
                PlayerStatistic stat = hcfPlayer.getPlayerStatistic();
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(), 0L, "", faction.getName(), faction.getDefaultRank().getName(), faction.getId()));
                faction.factionjoinLeftHistory.add(0, new HistoryEntrys.FactionJoinLeftEntry(p.getName(), "invited", new Date().getTime()));
                //NameChanger.refresh(p);

                NameChanger.refresh(p);
                NameChanger.refreshTeams(p);


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
