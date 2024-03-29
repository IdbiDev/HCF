package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.FactionHistory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

public class FactionCreateCommand extends SubCommand {
    public static Connection con = Main.getConnection();

    public static boolean isFactionNameTaken(String name) {
        return Main.nameToFaction.containsKey(name);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Creates a new faction.";
    }

    @Override
    public String getSyntax() {
        return "/faction create <name>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (Playertools.getPlayerFaction(p) == null) {
            String name = args[1];
            if(!Playertools.isValidName(args[1])) {
                p.sendMessage(Messages.prefix_cmd.language(p).queue() + " " + Messages.gui_bad_word.language(p).queue());
                return;
            }
            if (!isFactionNameTaken(name)) {
                for (String blacklisted_word : Main.blacklistedRankNames) {
                    if (name.toLowerCase().contains(blacklisted_word.toLowerCase())) {
                        p.sendMessage(Messages.prefix_cmd.language(p).queue() + " " + Messages.gui_bad_word.language(p).queue());
                        GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                        return;
                    }
                }

                //Create Faction
                int x = Playertools.getFreeFactionId();
                SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?', leader='?',ID = '?'", name, p.getUniqueId().toString(), String.valueOf(x));
                Faction faction = new Faction(x, name, p.getUniqueId().toString(), 0);
                faction.setPoints(Config.PointStart.asInt());
                Main.factionCache.put(x, faction);
                Main.nameToFaction.put(faction.getName(), faction);

                addCooldown(p);

                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                hcf.setFaction(faction);

                faction.saveFactionData();

                FactionRankManager.Rank defaultRank = FactionRankManager.create(p, "Default", false, true);
                FactionRankManager.Rank leaderRank = FactionRankManager.create(p, "Leader", true, false);
                hcf.setRank(leaderRank);


                SQL_Connection.dbExecute(con, "UPDATE members SET faction = ?, rank='?' WHERE uuid = '?'", String.valueOf(x), "Leader", p.getUniqueId().toString());

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
                faction.addMember(hcf);
                faction.refreshDTR();
                GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
                PlayerStatistic stat = hcf.getPlayerStatistic();
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(), 0L, "", faction.getName(), "Leader", faction.getId()));

                //HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID='?'", String.valueOf(faction.id));

                faction.loadFactionHistory(faction.assembleFactionHistory());

                faction.setDTR(faction.getDTR_MAX());
                NameChanger.refresh(p);

            } else {
                p.sendMessage(Messages.exists_faction_name.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
            }
        } else {
            p.sendMessage(Messages.you_already_in_faction.language(p).queue());
            GUISound.playSound(p, GUISound.HCFSounds.ERROR);
        }

    }
}
