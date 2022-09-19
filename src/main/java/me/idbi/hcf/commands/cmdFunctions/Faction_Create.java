package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Discord.LogLibrary;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

public class Faction_Create {
    public static Connection con = Main.getConnection("cmd.FactionCreate");

    public static void CreateFaction(Player p, String name) {
        if (playertools.getMetadata(p, "factionid").equalsIgnoreCase("0")) {
            if (!isFactionNameTaken(name)) {
                for(String blacklisted_word : Main.blacklistedRankNames){
                    if(name.toLowerCase().contains(blacklisted_word.toLowerCase())){
                        p.sendMessage(Messages.PREFIX.queue()+ " " +Messages.GUI_BAD_WORD.queue());
                        GUI_Sound.playSound(p,"error");
                        return;
                    }
                }
                //Create Faction
                int x = SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?', leader='?'", name, p.getUniqueId().toString());
                Main.Faction faction = new Main.Faction(x, name, p.getUniqueId().toString(), Main.faction_startingmoney);

                //
                playertools.setMetadata(p, "faction", name);
                playertools.setMetadata(p, "factionid", x);

                Main.faction_cache.put(x, faction);
                Main.factionToname.put(x, faction.factioname);
                Main.nameToFaction.put(faction.factioname, faction);

                Faction_Rank_Manager.Rank default_rank = Faction_Rank_Manager.CreateRank(faction, "Default");
                Faction_Rank_Manager.Rank leader_rank = Faction_Rank_Manager.CreateRank(faction, "Leader");
                assert default_rank != null;
                assert leader_rank != null;
                default_rank.isDefault = true;
                leader_rank.isLeader = true;
                faction.ApplyPlayerRank(p, leader_rank.name);

                SQL_Connection.dbExecute(con, "UPDATE members SET faction = ?,factionname='?',rank='?' WHERE uuid = '?'", String.valueOf(x), name, "leader", p.getUniqueId().toString());

                // Kiíratás global chatre ->
                //                              xy faction létre jött
                Bukkit.broadcastMessage(Messages.FACTION_CREATON.getMessage().setFaction(name).repPlayer(p).queue());
//                displayTeams.createTeam(faction);
//                displayTeams.addPlayerToTeam(p);
                Scoreboards.refresh(p);
                LogLibrary.sendFactionCreate(p, faction.factioname);
                faction.refreshDTR();
                GUI_Sound.playSound(p,"success");

            } else {
                p.sendMessage(Messages.EXISTS_FACTION_NAME.getMessage().queue());
                GUI_Sound.playSound(p,"error");
            }
        } else {
            p.sendMessage(Messages.YOU_ALREADY_IN_FACTION.getMessage().queue());
            GUI_Sound.playSound(p,"error");
        }

    }

    public static boolean isFactionNameTaken(String name) {
        HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE name='?'", name);
        return (factionMap.size() > 0);
    }
}
