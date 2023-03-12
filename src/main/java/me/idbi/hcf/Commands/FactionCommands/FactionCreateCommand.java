package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
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
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction create <name>";
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
            String name = args[1];
            if(!Playertools.isValidName(args[1])) {
                p.sendMessage("Nem valid név!");
                return;
            }
            if (!isFactionNameTaken(name)) {
                for (String blacklisted_word : Main.blacklistedRankNames) {
                    if (name.toLowerCase().contains(blacklisted_word.toLowerCase())) {
                        p.sendMessage(Messages.prefix_cmd.language(p).queue() + " " + Messages.gui_bad_word.language(p).queue());
                        GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                        return;
                    }
                }
                //Create Faction
                int x = Playertools.getFreeFactionId();
                SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?', leader='?',ID = '?'", name, p.getUniqueId().toString(), String.valueOf(x));
                Faction faction = new Faction(x, name, p.getUniqueId().toString(), 0);
                //

                Main.factionCache.put(x, faction);
                Main.nameToFaction.put(faction.name, faction);

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
                faction.members.add(hcf);
                faction.refreshDTR();
                GUI_Sound.playSound(p, GUI_Sound.HCFSounds.SUCCESS);
                PlayerStatistic stat = hcf.playerStatistic;
                stat.factionHistory.add(0, new FactionHistory(new Date().getTime(), 0L, "", faction.name, "Leader", faction.id));

                //HashMap<String, Object> factionMap = SQL_Connection.dbPoll(con, "SELECT * FROM factions WHERE ID='?'", String.valueOf(faction.id));

                faction.loadFactionHistory(faction.assembleFactionHistory());

                faction.DTR = faction.DTR_MAX;
                NameChanger.refresh(p);

            } else {
                p.sendMessage(Messages.exists_faction_name.language(p).queue());
                GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
            }
        } else {
            p.sendMessage(Messages.you_already_in_faction.language(p).queue());
            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
        }

    }
}
