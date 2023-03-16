package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Scoreboards {
    private static final Main m = Main.getPlugin(Main.class);
    private static MiscTimers miscTimers = new MiscTimers();
    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static void refresh(Player p) {
        if (Playertools.isInStaffDuty(p)) {
            AdminScoreboard.refresh(p);
            return;
        }

        List<String> mainScoreboard = new ArrayList<>();
        for (String s : Config.DefaultScoreboard.asStrList()) {
            mainScoreboard.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        List<String> replacedList = new ArrayList<>();
        for (String line : mainScoreboard) {
            if (line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if (customTimers.getValue().isActive()) {
                        replacedList.add(line.replace("%customtimers%", replaceVariables(customTimers.getValue().getFormatted(), p)));
                    }
                }
                continue;
            }
            if(isContinue(p, line)) continue;

            replacedList.add(replaceVariables(line, p));
        }

        if(replacedList.get(replacedList.size() - 1).equals("") || replacedList.get(replacedList.size() - 1).equalsIgnoreCase(" ")) {
            replacedList = replacedList.subList(0, replacedList.size() - 1);
        }

        ArrayList<String> newReplacedList = new ArrayList<>();
        for (String s : replacedList) {
            if(s.length() > 30) {
                newReplacedList.add(s.substring(0, 30));
                continue;
            }
            newReplacedList.add(s);
        }

        FastBoard board = Main.boards.get(p.getUniqueId());
        board.updateTitle(Config.DefaultScoreboardTitle.asStr());
        if(board == null) {
            FastBoard newBoard = new FastBoard(p);
            newBoard.updateTitle(Config.DefaultScoreboardTitle.asStr());
            newBoard.updateLines(newReplacedList);
            Main.boards.put(p.getUniqueId(), newBoard);
            return;
        }
        board.updateLines(newReplacedList);
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
        } else if (line.contains("%eotw%") && HCF_Timer.getEOTWTimer(p) <= 0L) {
            return true;
        } else if (line.contains("%sotw%") && HCF_Timer.getSOTWTime(p) <= 0L) {
            return true;
        } else if (line.contains("%logout%") && HCF_Timer.getLogoutTime(p) <= 0L) {
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
                    || line.contains("%eotw%")
                    || line.contains("%customtimers%")
                    || line.contains("%logout%"))
                    {
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
                .replace("%logout%", getDouble(HCF_Timer.getLogoutTime(p)))
                .replace("%ep_cd%", getDouble(HCF_Timer.getEpTime(p)))
                .replace("%gapple_cd%", getDouble(HCF_Timer.get_Golden_Apple_Time(p)))
                .replace("%opgapple_cd%", ConvertTime((int) (HCF_Timer.get_OP_Golden_Apple_Time(p) / 1000)))
                .replace("%eotw%", ConvertTime((int) HCF_Timer.getEOTWTimer(p) / 1000))
                .replace("%sotw%", ConvertTime((int) HCF_Timer.getSOTWTime(p) / 1000))
                .replace("%location%", hcf.getLocationFormatted() == null ? Messages.wilderness.language(p).queue() : hcf.getLocationFormatted());
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
