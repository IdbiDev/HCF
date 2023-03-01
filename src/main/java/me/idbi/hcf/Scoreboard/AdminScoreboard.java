package me.idbi.hcf.Scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
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
    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static void refresh(Player p) {
        List<String> fix = sortLists().get(0);
        List<String> timers = sortLists().get(1);

        sortLists();

        HCFPlayer player = HCFPlayer.getPlayer(p);

        Collections.reverse(fix);
        Collections.reverse(timers);

        ScoreboardBuilder builder = ScoreboardBuilder.getOrCreate(p);
        builder.setDisplayName(Config.StaffDutyScoreboardTitle.asStr());

        int scoreNumber = 0;

        int emptyCalc = 0;
        for (String line : timers) {
            if (line.contains("%spawntag%") && HCF_Timer.getCombatTime(p) <= 0.0) {
                continue;
            } else if (line.contains("%ep_cd%") && HCF_Timer.getEpTime(p) <= 0.0) {
                continue;
            } else if (line.contains("%stuck_timer%") && HCF_Timer.getStuckTime(p) <= 0.0) {
                continue;
            } else if (line.contains("%bard_energy%") && player.bardEnergy <= 0.0) {
                continue;
            } else if (line.contains("%eotw%") && MiscTimers.getTimeOfEOTW() <= 0L) {
                continue;
            } else if (line.contains("%gapple_cd%") && HCF_Timer.get_Golden_Apple_Time(p) <= 0L) {
                continue;
            } else if (line.contains("%opgapple_cd%") && HCF_Timer.get_OP_Golden_Apple_Time(p) <= 0L) {
                continue;
            }

            if (line.equals("empty")) {
                if (scoreNumber == 0) {
                    continue;
                }
                line = line.replace("empty", "");
                for (int i = 0; i <= emptyCalc; i++) {
                    line += " ";
                }
                emptyCalc++;
            }

            if (line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if (customTimers.getValue().isActive()) {
                        Score score = obj.getScore(line.replace("%customtimers%",
                                Scoreboards.replaceVariables(customTimers.getValue().getFormatted(), p)));
                        score.setScore(scoreNumber);
                        scoreNumber++;
                    }
                }
                continue;
            }

            Score score = obj.getScore(replaceVariablesAdmin(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        int emptyCalc2 = 0;
        for (String line : fix) {
            if (line.equals("empty")) {
                if (scoreNumber == 0) {
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
            if (Playertools.isInStaffDuty(p))
                AdminScoreboard.refresh(p);
            else Scoreboards.refresh(p);
        }
    }

    /*
      - "&4* &cStaff Panel"
  - "&f* &7Visibility: &f%invisible%"
  - "&f* &7Chatmode: &f%chat_mode%"
  - "&f* &7Players: &f%online_players%"
  - "&f* &7TPS: &a%tps%"
     */

    public static ArrayList<List<String>> sortLists() {
        List<String> str = Config.StaffScoreboard.asStrList();
        List<String> fix = new ArrayList<>();
        List<String> timers = new ArrayList<>();
        for (String line : str) {
            if (line.contains("%spawntag%")
                    || line.contains("%ep_cd%")
                    || line.contains("%bard_energy%")
                    || line.contains("%stuck_timer%")
                    || line.contains("%gapple_cd%")
                    || line.contains("%pvp_timer%")
                    || line.contains("%opgapple_cd%")
                    || line.contains("%sotw%")
                    || line.contains("%customtimers%")
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

    public static String replaceVariablesAdmin(String inputString, Player p) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        return PlaceholderAPI.setPlaceholders(p,
                Scoreboards.replaceVariables(inputString, p)
                        .replace("%invisible%", (AdminTools.InvisibleManager.invisedAdmins.contains(p) ? "§a✔" : "§c✖"))
                        .replace("%chat_mode%", player.staffChat ? Messages.admin_staff_chat.language(p).queue() : Messages.admin_global_chat.language(p).queue())
                        .replace("%online_players%", Bukkit.getOnlinePlayers().size() + "")
                        .replace("%tps%", dfSharp.format(Bukkit.spigot().getTPS()[0]) + ""));
    }
}
