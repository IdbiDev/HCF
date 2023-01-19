package me.idbi.hcf.Scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.AdminTools;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.Misc_Timers;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminScoreboard {

    private static final Main m = Main.getPlugin(Main.class);


    public static void refresh(Player p) {
        List<String> fix = sortLists().get(0);
        List<String> timers = sortLists().get(1);

        sortLists();

        Collections.reverse(fix);
        Collections.reverse(timers);

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        //ScoreboardManager manager = Bukkit.getScoreboardManager();

        Objective obj = sb.registerNewObjective("dummy", "igen");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.translateAlternateColorCodes('&', m.getConfig().getString("Admin_Scoreboard_Title")));

        int scoreNumber = 0;

        int emptyCalc = 0;
        for (String line : timers) {
            if (line.contains("%spawntag%") && HCF_Timer.getCombatTime(p) <= 0.0) {
                continue;
            } else if (line.contains("%ep_cd%") && HCF_Timer.getEpTime(p) <= 0.0) {
                continue;
            } else if (line.contains("%stuck_timer%") && HCF_Timer.getStuckTime(p) <= 0.0) {
                continue;
            } else if(line.contains("%bard_energy%") && Double.parseDouble(playertools.getMetadata(p, "bardenergy")) <= 0.0) {
                continue;
            } else if(line.contains("%eotw%") && Misc_Timers.getTimeOfEOTW() <= 0L) {
                continue;
            } else if(line.contains("%gapple_cd%") && HCF_Timer.get_Golden_Apple_Time(p) <= 0L) {
                continue;
            } else if(line.contains("%opgapple_cd%") && HCF_Timer.get_OP_Golden_Apple_Time(p) <= 0L) {
                continue;
            }

            if (line.equals("empty")) {
                if(scoreNumber == 0) {
                    continue;
                }
                line = line.replace("empty", "");
                for (int i = 0; i <= emptyCalc; i++) {
                    line += " ";
                }
                emptyCalc++;
            }

            Score score = obj.getScore(replaceVariablesAdmin(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        int emptyCalc2 = 0;
        for (String line : fix) {
            if (line.equals("empty")) {
                if(scoreNumber == 0) {
                    continue;
                }
                line = line.replace("empty", "");
                for (int i = 0; i <= emptyCalc2; i++) {
                    line += " ";
                }
                emptyCalc2++;
            }

            Score score = obj.getScore(replaceVariablesAdmin(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        p.setScoreboard(sb);
    }

    public static void RefreshAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if(playertools.isInStaffDuty(p))
                AdminScoreboard.refresh(p);
            else Scoreboards.refresh(p);
        }
    }

    public static ArrayList<List<String>> sortLists() {
        List<String> str = m.getConfig().getStringList("Admin_Scoreboard");
        List<String> fix = new ArrayList<>();
        List<String> timers = new ArrayList<>();
        for (String line : str) {
            if (line.contains("%spawntag%")
                    || line.contains("%ep_cd%")
                    || line.contains("%bard_energy%")
                    || line.contains("%stuck_timer%")
                    || line.contains("%gapple_cd%")
                    || line.contains("%opgapple_cd%")
                    || line.contains("%eotw%")) {
                timers.add(ChatColor.translateAlternateColorCodes('&', line));
                continue;
            }
            fix.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        ArrayList<List<String>> returnList = new ArrayList<>();
        returnList.add(fix);
        returnList.add(timers);
        return returnList;
    }

    /*
      - "&4* &cStaff Panel"
  - "&f* &7Visibility: &f%invisible%"
  - "&f* &7Chatmode: &f%chat_mode%"
  - "&f* &7Players: &f%online_players%"
  - "&f* &7TPS: &a%tps%"
     */

    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");
    public static String replaceVariablesAdmin(String inputString, Player p) {
        return PlaceholderAPI.setPlaceholders(p,
                Scoreboards.replaceVariables(inputString, p)
                .replace("%invisible%", (AdminTools.InvisibleManager.invisedAdmins.contains(p) ? "§a✔" : "§c✖"))
                .replace("%chat_mode%", (Boolean.parseBoolean(playertools.getMetadata(p, "staffchat")) ? "§aStaff Chat" : "§bGlobal Chat"))
                .replace("%online_players%", Bukkit.getOnlinePlayers().size() + "")
                .replace("%tps%", dfSharp.format(Bukkit.spigot().getTPS()[0]) + ""))
                ;
    }
}
