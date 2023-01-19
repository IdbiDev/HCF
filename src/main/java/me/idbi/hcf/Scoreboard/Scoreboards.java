package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.*;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scoreboards {
    private static final Main m = Main.getPlugin(Main.class);

    public static void refresh(Player p) {
        if(playertools.isInStaffDuty(p)) {
            AdminScoreboard.refresh(p);
            return;
        }

        List<String> fix = sortLists().get(0);
        List<String> timers = sortLists().get(1);

        sortLists();

        Faction fac = playertools.getPlayerFaction(p);

        Collections.reverse(fix);
        Collections.reverse(timers);

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        //ScoreboardManager manager = Bukkit.getScoreboardManager();

        Objective obj = sb.registerNewObjective("dummy", "igen");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ConfigLibrary.Scoreboard_title.getValue());

        int scoreNumber = 0;

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
            } else if(line.contains("%sotw%") && Misc_Timers.getTimeOfSOTW() <= 0L) {
                continue;
            } else if(line.contains("%pvp_timer%") && HCF_Timer.getPvPTimerCoolDownSpawn(p) <= 0L) {
                continue;
            } else if(line.contains("%gapple_cd%") && HCF_Timer.get_Golden_Apple_Time(p) <= 0L) {
                continue;
            } else if(line.contains("%opgapple_cd%") && HCF_Timer.get_OP_Golden_Apple_Time(p) <= 0L) {
                continue;
            }

            if (line.equals("empty"))
                line = " ";

            if(line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if(customTimers.getValue().isActive()) {
                        Score score = obj.getScore(replaceVariables(customTimers.getValue().getFormatted(), p));
                        score.setScore(scoreNumber);
                    }
                }
                continue;
            }

            Score score = obj.getScore(replaceVariables(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        int emptyCalc = 0;
        for (String line : fix) {
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

            Score score = obj.getScore(replaceVariables(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        p.setScoreboard(sb);
        if(fac == null) return;
        //fac.addPrefixPlayer(p);
        //displayTeams.refreshPlayer(p);
//        if(!playertools.getMetadata(p, "factionid").equals("0")) {
//            displayTeams.addPlayerToTeam(p);
//            return;
//        }
//
//        displayTeams.removePlayerFromTeam(p);
    }

    public static void RefreshAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboards.refresh(p);
            NameChanger.refresh(p);
        }
    }

    public static ArrayList<List<String>> sortLists() {
        List<String> str = ConfigLibrary.getScoreboard();
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

    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static String replaceVariables(String inputString, Player p) {
        return inputString
                .replace("%money%", playertools.getMetadata(p, "money"))
                .replace("%faction%", playertools.getMetadata(p, "faction"))
                .replace("%class%", playertools.getMetadata(p, "class"))
                .replace("%bard_energy%", dfSharp.format(
                        Double.parseDouble(
                                playertools.getMetadata(p, "bardenergy")))
                        .replace(",", "."))
//                        new SimpleDateFormat("s").format(
//                                new Date((long) (Double.parseDouble(playertools.getMetadata(p, "bardenergy")) * 1000L)))))
                //.replace("%bard_energy%", new SimpleDateFormat("s").format(new Date()))
                .replace("%spawntag%", getDouble(HCF_Timer.getCombatTime(p)))
                .replace("%pvp_timer%", getDouble(HCF_Timer.getPvPTimerCoolDownSpawn(p)))
                .replace("%stuck_timer%", getDouble(HCF_Timer.getStuckTime(p)))
                .replace("%ep_cd%", getDouble(HCF_Timer.getEpTime(p)))
                .replace("%gapple_cd%", getDouble(HCF_Timer.get_Golden_Apple_Time(p)))
                .replace("%opgapple_cd%", ConvertTime((int) (HCF_Timer.get_OP_Golden_Apple_Time(p) / 1000)))
                .replace("%eotw%", ConvertTime((int) Misc_Timers.getTimeOfEOTW() /1000))
                .replace("%sotw%", ConvertTime((int) Misc_Timers.getTimeOfSOTW()/1000))
                .replace("%location%", HCF_Claiming.sendFactionTerritory(p));
    }

    public static String getDouble(long value) {
        return String.format("%.1f", Double.parseDouble(new SimpleDateFormat("ss.SSS").format(new Date(value)))).replace(",", ".");
    }

    public static String ConvertTime(int seconds) {
        if(seconds <= 0)
            return "0";

//        int day = (int) TimeUnit.MINUTES.toDays(minutes);
//        long hours = TimeUnit.MINUTES.toHours(minutes) - (day * 24);
//        long minute = TimeUnit.MINUTES.toMinutes(minutes) - (TimeUnit.MINUTES.toHours(minutes) * 60);
//        int day = (int)TimeUnit.SECONDS.toDays(seconds);
//        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
//        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
//        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        int minute = 0;
        int hours = 0;
        int second = seconds;
        if(seconds > 60) {
            second = seconds % 60;
            minute = (seconds / 60) % 60;
            hours = (seconds/60)/60;
        }

        String strSec= Integer.toString(second);
        String strmin= Integer.toString(minute);
        String strHours= Integer.toString(hours);

        String result = "";

        if (hours != 0) {
            result += hours;
            result += ":";
        }

        if (minute != 0) {
            if(minute < 10)
                result += "0" + minute;
            else
                result += minute;

            result += ":";
        } else
            result += "00:";

        if(seconds < 10)
            result += "0" + second;
        else
            result += second;

        //return result;
        return result;
    }
}
