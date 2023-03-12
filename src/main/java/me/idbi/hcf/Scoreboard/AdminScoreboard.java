package me.idbi.hcf.Scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Lag;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminScoreboard {

    private static final MiscTimers miscTimers = new MiscTimers();
    private static final Main m = Main.getPlugin(Main.class);
    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static void refresh(Player p) {
        List<String> mainScoreboard = new ArrayList<>();
        for (String s : Config.StaffScoreboard.asStrList()) {
            mainScoreboard.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        int emptyCalc = 0;
        List<String> replacedList = new ArrayList<>();
        for (String line : mainScoreboard) {
            if (line.equals("empty")) {
                line = line.replace("empty", "");
                for (int i = 0; i <= emptyCalc; i++) {
                    line += " ";
                }
                emptyCalc++;
            }

            if (line.contains("%customtimers%")) {
                for (Map.Entry<String, CustomTimers> customTimers : Main.customSBTimers.entrySet()) {
                    if (customTimers.getValue().isActive()) {
                        replacedList.add(line.replace("%customtimers%",
                                Scoreboards.replaceVariables(customTimers.getValue().getFormatted(), p)));
                    }
                }
                continue;
            }
            if (Scoreboards.isContinue(p, line)) continue;

            replacedList.add(replaceVariablesAdmin(line, p));
        }

        if (replacedList.get(replacedList.size() - 1).equalsIgnoreCase("") || replacedList.get(replacedList.size() - 1).equalsIgnoreCase(" ")) {
            replacedList = replacedList.subList(0, replacedList.size() - 1);
        }

        ArrayList<String> newReplacedList = new ArrayList<>();
        for (String s : replacedList) {
            if (s.length() > 30) {
                newReplacedList.add(s.substring(0, 30));
                continue;
            }
            newReplacedList.add(s);
        }

        FastBoard board = Main.boards.get(p.getUniqueId());
        board.updateTitle(Config.StaffDutyScoreboardTitle.asStr());
        if (board == null) {
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
                        .replace("%chat_mode%", player.getFormattedChatType())
                        .replace("%online_players%", Bukkit.getOnlinePlayers().size() + "")
                        .replace("%tps%", dfSharp.format(Lag.getTPS()) + ""));
    }
}

