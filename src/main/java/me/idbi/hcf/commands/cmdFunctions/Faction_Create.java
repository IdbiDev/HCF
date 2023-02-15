package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.AsyncSQL;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.FactionHistory;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

public class Faction_Create {
    public static Connection con = Main.getConnection("cmd.FactionCreate");

    public static void CreateFaction(Player p, String name) {
        if (playertools.getPlayerFaction(p) == null) {
            if (!isFactionNameTaken(name)) {
                for(String blacklisted_word : Main.blacklistedRankNames){
                    if(name.toLowerCase().contains(blacklisted_word.toLowerCase())){
                        p.sendMessage(Messages.prefix_cmd.language(p).queue()+ " " + Messages.gui_bad_word.language(p).queue());
                        GUI_Sound.playSound(p,"error");
                        return;
                    }
                }
                //Create Faction
                int x = playertools.getFreeFactionId();
                SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?', leader='?',ID = '?'", name, p.getUniqueId().toString(), String.valueOf(x));
                Faction faction = new Faction(x, name, p.getUniqueId().toString(), 0);
                    //

                Main.faction_cache.put(x, faction);
                Main.nameToFaction.put(faction.name, faction);

                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                hcf.setFaction(faction);

                Faction_Rank_Manager.Rank defaultRank = Faction_Rank_Manager.create(p, "Default");
                assert defaultRank != null;
                defaultRank.isDefault = true;
                defaultRank.priority   = -9999;
                //defaultRank.saveRank();
                Faction_Rank_Manager.Rank leaderRank = Faction_Rank_Manager.create(p, "Leader");
                assert leaderRank != null;
                leaderRank.isLeader   = true;
                leaderRank.priority    =  9999;
                hcf.setRank(leaderRank);
                //leaderRank.saveRank();


                    SQL_Connection.dbExecute(con, "UPDATE members SET faction = ?,rank='?' WHERE uuid = '?'", String.valueOf(x), "Leader", p.getUniqueId().toString());

                    // Kiíratás global chatre ->
                    //                              xy faction létre jött
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendMessage(Messages.faction_creation.language(onlinePlayer).setFaction(name).setPlayer(p).queue());
                    }

                    // displayTeams.createTeam(faction);
                    // displayTeams.addPlayerToTeam(p);
                    //faction.addPrefixPlayer(p);

                    Scoreboards.refresh(p);
                    // LogLibrary.sendFactionCreate(p, faction.name);
                    faction.members.add(hcf);
                    faction.refreshDTR();
                    GUI_Sound.playSound(p,"success");
                    PlayerStatistic stat = hcf.playerStatistic;
                    stat.factionHistory.add(0, new FactionHistory(new Date().getTime(),0L,"", faction.name, "Leader", faction.id));

                    //HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID='?'", String.valueOf(faction.id));
                    faction.loadFactionHistory(faction.assembleFactionHistory());
                    faction.saveFactionData();
                    faction.DTR = faction.DTR_MAX;
                    NameChanger.refresh(p);

            } else {
                p.sendMessage(Messages.exists_faction_name.language(p).queue());
                GUI_Sound.playSound(p,"error");
            }
        } else {
            p.sendMessage(Messages.you_already_in_faction.language(p).queue());
            GUI_Sound.playSound(p,"error");
        }

    }

    public static boolean isFactionNameTaken(String name) {
        HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE name='?'", name);
        return (factionMap.size() > 0);
    }
}
