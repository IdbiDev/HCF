package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Discord.LogLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

public class Faction_Create {
    public static Connection con = Main.getConnection("cmd.FactionCreate");

    public static void CreateFaction(Player p, String name) {
        if (playertools.getMetadata(p, "factionid").equalsIgnoreCase("0")) {
            if (!isFactionNameTaken(name)) {
                //Create Faction
                int x = SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?', leader='?'", name, p.getUniqueId().toString());
                //Todo: GetFactionDefault Balance
                Main.Faction faction = new Main.Faction(x, name, p.getUniqueId().toString(), Main.faction_startingmoney);

                //
                playertools.setMetadata(p, "faction", name);
                playertools.setMetadata(p, "factionid", x);

                Main.faction_cache.put(x, faction);
                Main.factionToname.put(x, faction.factioname);
                Main.nameToFaction.put(faction.factioname, faction);

                rankManager.Faction_Rank default_rank = rankManager.CreateNewRank(faction, "default");
                rankManager.Faction_Rank leader_rank = rankManager.CreateNewRank(faction, "leader");
                rankManager.setDefaultRank(faction, default_rank.name);
                rankManager.setLeaderRank(faction, leader_rank.name);
                faction.ApplyPlayerRank(p, leader_rank.name);

                SQL_Connection.dbExecute(con, "UPDATE members SET faction = ?,factionname='?',rank='?' WHERE uuid = '?'", String.valueOf(x), name, "leader", p.getUniqueId().toString());

                LogLibrary.sendFactionCreate(p, faction.factioname);
                // Kiíratás global chatre ->
                //                              xy faction létre jött
                Bukkit.broadcastMessage(Messages.FACTION_CREATON.getMessage().setFaction(name).repPlayer(p).queue());

//                displayTeams.createTeam(faction);
//                displayTeams.addPlayerToTeam(p);
                Scoreboards.refresh(p);

            } else {
                p.sendMessage(Messages.EXISTS_FACTION_NAME.getMessage().queue());
            }
        } else {
            p.sendMessage(Messages.YOU_ALREADY_IN_FACTION.getMessage().queue());
        }

    }

    public static boolean isFactionNameTaken(String name) {
        HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE name='?'", name);
        return (factionMap.size() > 0);
    }
}
