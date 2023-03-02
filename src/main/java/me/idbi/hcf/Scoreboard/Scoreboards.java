package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Objects.Faction;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class Scoreboards {
    private static final Main m = Main.getPlugin(Main.class);
    private static MiscTimers miscTimers = new MiscTimers();
    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static void refresh(Player p) {
        if (Playertools.isInStaffDuty(p)) {
            AdminScoreboard.refresh(p);
            return;
        }

        ScoreboardBuilder builder = ScoreboardBuilder.getOrCreate(p);
        builder.setDisplayName(Config.DefaultScoreboardTitle.asStr());
/*        ScoreboardSign sign = ScoreboardSign.getScoreboard(p);
        sign.setObjectiveName(Config.ScoreboardTitle.asStr());*/
        //sign.create();

        List<String> fix = sortLists().get(0);
        List<String> timers = sortLists().get(1);

        List<String> tryThis = new ArrayList<>();
        for (String s : Config.DefaultScoreboard.asStrList()) {
            tryThis.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        sortLists();

        Faction fac = Playertools.getPlayerFaction(p);
        HCFPlayer player = HCFPlayer.getPlayer(p);

        Collections.reverse(tryThis);
        Collections.reverse(fix);
        Collections.reverse(timers);

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        //ScoreboardManager manager = Bukkit.getScoreboardManager();

        Objective obj = sb.registerNewObjective("dummy", "igen");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(Config.DefaultScoreboardTitle.asStr());

        int scoreNumber = 1;

        int emptyCalcTimer = 0;
        ArrayList<String> replacedList = new ArrayList<>();
        HashMap<Integer, String> lines = new HashMap<>();
/*        for (String line : timers) {
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
            } else if (line.contains("%sotw%") && MiscTimers.getTimeOfSOTW() <= 0L) {
                continue;
            } else if (line.contains("%pvp_timer%") && HCF_Timer.getPvPTimerCoolDownSpawn(p) <= 0L) {
                continue;
            } else if (line.contains("%gapple_cd%") && HCF_Timer.get_Golden_Apple_Time(p) <= 0L) {
                continue;
            } else if (line.contains("%opgapple_cd%") && HCF_Timer.get_OP_Golden_Apple_Time(p) <= 0L) {
                continue;
            }

            if (line.equals("empty")) {
                *//*if (scoreNumber == 0) {
                    *//**//*builder.setLine(scoreNumber, replaceVariables(line, p));
                    scoreNumber++;
                    continue;*//**//*
                }*//*
                line = line.replace("empty", "");
                for (int i = 0; i <= emptyCalcTimer; i++) {
                    line += " ";
                }
                emptyCalcTimer++;
            }

            if (line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if (customTimers.getValue().isActive()) {
                        *//*Score score = obj.getScore(line.replace("%customtimers%",
                                replaceVariables(customTimers.getValue().getFormatted(), p)));
                        score.setScore(scoreNumber);*//*
                        *//*sign.setLine(scoreNumber, line.replace("%customtimers%",
                                replaceVariables(customTimers.getValue().getFormatted(), p)));*//*
                        *//*builder.setLine(scoreNumber, line.replace("%customtimers%",
                                replaceVariables(customTimers.getValue().getFormatted(), p)));*//*
                        lines.put(scoreNumber, line.replace("%customtimers%",
                                replaceVariables(customTimers.getValue().getFormatted(), p)));
                        scoreNumber++;
                    }
                }
                continue;
            }

            //sign.setLine(scoreNumber, replaceVariables(line, p));
            // Score score = obj.getScore(replaceVariables(line, p));
            //score.setScore(scoreNumber);

            //builder.setLine(scoreNumber, replaceVariables(line, p));
            lines.put(scoreNumber, replaceVariables(line, p));
            scoreNumber++;
        }*/

        for (String line : tryThis) {
            if (line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if (customTimers.getValue().isActive()) {
                        //builder.setLine(scoreNumber, line.replace("%customtimers%", replaceVariables(customTimers.getValue().getFormatted(), p)));
                        replacedList.add(line.replace("%customtimers%", replaceVariables(customTimers.getValue().getFormatted(), p)));
                        scoreNumber++;
                    }
                }
                continue;
            }
            if(isContinue(p, line)) continue;

            //builder.setLine(scoreNumber, replaceVariables(line, p));
            replacedList.add(replaceVariables(line, p));
            scoreNumber++;
        }
        for (int i = scoreNumber; i < Math.max(Config.DefaultScoreboard.asStrList().size(), Config.StaffScoreboard.asStrList().size()); i++) {
            //builder.removeLine(i);
        }

        ScoreboardLib lib = ScoreboardLib.start(m, new ScoreboardLib.ScoreboardTextProvider() {
            @Override
            public List<String> getScoreboardText() {
                return replacedList;
            }

            @Override
            public String getScoreboardTitle() {
                return "Title";
            }

            @Override
            public String format(Player p, String row) {
                return row;
            }
        }, 20L);
        lib.addPlayer(p);
    }

    public static void RefreshAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboards.refresh(p);
            NameChanger.refresh(p);
        }
    }

    public static boolean isContinue(Player p, String line) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (line.contains("%spawntag%") && HCF_Timer.getCombatTime(p) <= 0.0) {
            return true;
        } else if (line.contains("%ep_cd%") && HCF_Timer.getEpTime(p) <= 0.0) {
            return true;
        } else if (line.contains("%stuck_timer%") && HCF_Timer.getStuckTime(p) <= 0.0) {
            return true;
        } else if (line.contains("%bard_energy%") && player.bardEnergy <= 0.0) {
            return true;
        } else if (line.contains("%eotw%") && miscTimers.getTimeOfEOTW() <= 0L) {
            return true;
        } else if (line.contains("%sotw%") && miscTimers.getTimeOfSOTW() <= 0L) {
            return true;
        } else if (line.contains("%pvp_timer%") && HCF_Timer.getPvPTimerCoolDownSpawn(p) <= 0L) {
            return true;
        } else if (line.contains("%gapple_cd%") && HCF_Timer.get_Golden_Apple_Time(p) <= 0L) {
            return true;
        } else if (line.contains("%opgapple_cd%") && HCF_Timer.get_OP_Golden_Apple_Time(p) <= 0L) {
            return true;
        }
        return false;
    }

    public static ArrayList<List<String>> sortLists() {
        List<String> str = Config.DefaultScoreboard.asStrList();
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

    public static String replaceVariables(String inputString, Player p) {
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        return inputString
                .replace("%money%", hcf.money + "")
                .replace("%faction%", hcf.getFactionName())
                .replace("%class%", Playertools.upperFirst(hcf.playerClass.name()))
                .replace("%bard_energy%", dfSharp.format(
                                hcf.bardEnergy)
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
                .replace("%eotw%", ConvertTime((int) miscTimers.getTimeOfEOTW() / 1000))
                .replace("%sotw%", ConvertTime((int) miscTimers.getTimeOfSOTW() / 1000))
                .replace("%location%", hcf.getLocationFormatted());
    }

    public static String getDouble(long value) {
        return String.format("%.1f", Double.parseDouble(new SimpleDateFormat("ss.SSS").format(new Date(value)))).replace(",", ".");
    }

    public static String ConvertTime(int seconds) {
        if (seconds <= 0)
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
        if (seconds > 60) {
            second = seconds % 60;
            minute = (seconds / 60) % 60;
            hours = (seconds / 60) / 60;
        }

        String strSec = Integer.toString(second);
        String strmin = Integer.toString(minute);
        String strHours = Integer.toString(hours);

        String result = "";

        if (hours != 0) {
            result += hours;
            result += ":";
        }

        if (minute != 0) {
            if (minute < 10)
                result += "0" + minute;
            else
                result += minute;

            result += ":";
        } else
            result += "00:";

        if (second < 10)
            result += "0" + second;
        else
            result += second;

        //return result;
        return result;
    }
}
