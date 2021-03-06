package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scoreboards {
    private static final Main m = Main.getPlugin(Main.class);

    public static void scoreboard() {
        List<String> fix = sortLists().get(0);
        List<String> timers = sortLists().get(1);

        sortLists();

        Collections.reverse(fix);
        Collections.reverse(timers);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getWorld(ConfigLibrary.World_name.getValue()).getPlayers()) {
                    Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();


                    Objective obj = sb.registerNewObjective("dummy", "igen");
                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                    obj.setDisplayName(ConfigLibrary.Scoreboard_title.getValue());

                    int scoreNumber = 0;

                    for (String line : timers) {

                        if (line.equals("empty"))
                            line = "";

                        Score score = obj.getScore(replaceVariables(line, p));
                        score.setScore(scoreNumber);

                        scoreNumber++;
                    }


                    for (String line : fix) {
                        if (line.equals("empty"))
                            line = " ";

                        Score score = obj.getScore(replaceVariables(line, p));
                        score.setScore(scoreNumber);

                        scoreNumber++;
                    }

                    p.setScoreboard(sb);
                }
            }
        }.runTaskTimer(m, 0L, 60L);
    }

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
            }

            if (line.equals("empty"))
                line = " ";

            Score score = obj.getScore(replaceVariables(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }


        for (String line : fix) {
            if (line.equals("empty")) {
                if(scoreNumber == 0) {
                    continue;
                }
                line = "  ";
            }

            Score score = obj.getScore(replaceVariables(line, p));
            score.setScore(scoreNumber);

            scoreNumber++;
        }

        p.setScoreboard(sb);
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
        }
    }

    public static ArrayList<List<String>> sortLists() {
        List<String> str = ConfigLibrary.getScoreboard();
        List<String> fix = new ArrayList<>();
        List<String> timers = new ArrayList<>();
        for (String line : str) {
            if (line.contains("%spawntag%") || line.contains("%ep_cd%") || line.contains("%bard_energy%") || line.contains("%stuck_timer%")) {
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

    private static String replaceVariables(String inputString, Player p) {
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
                .replace("%stuck_timer%", getDouble(HCF_Timer.getStuckTime(p)))
                .replace("%ep_cd%", getDouble(HCF_Timer.getEpTime(p)))
                .replace("%location%", HCF_Claiming.sendFactionTerritory(p));
    }

    public static String getDouble(long value) {
        return String.format("%.1f", Double.parseDouble(new SimpleDateFormat("ss.SSS").format(new Date(value)))).replace(",", ".");
    }
}
