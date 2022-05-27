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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Scoreboards {

    /*

public class scoreboard {

    private Main m = Main.getPlugin(Main.class);

    public scoreboard() {

        ArrayList<String > scoreboardList = new ArrayList<String>() {{
            add("§7┌─");
            add("§7│ §eFaction: §6%faction%");
            add("§7│ §eLocation: §6%location%");
            add("§7│ §eMoney: §6$%money%");
            add("§7│ §eClass: §6%class%");
            add("§7└─");
        }};

        ArrayList<String> belowSb = new ArrayList<String>() {{
            add("empty");
            add("§7▍ §eSpawn Tag: §6%spawntag%");
            add("§7▍ §ePearl: §6%ep_cd%");
            add("§7▍ §eBard energy: §6%bard_energy%");
        }};

        Collections.reverse(belowSb);
        Collections.reverse(scoreboardList);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    Objective obj = scoreboard.registerNewObjective("test", "dummy");

                    obj.setDisplaySlot(DisplaySlot.SIDEBAR);

                    obj.setDisplayName("§cCukiMC §7- §eMap #1");

                    int scoreNumber = 0;

                    for(String line : belowSb) {

                        if(line.equals("empty"))
                            line = "";

                        Score score = obj.getScore(replaceVariables(line, p));
                        score.setScore(scoreNumber);

                        scoreNumber++;
                    }

                    for(String line : scoreboardList) {
                        if(line.equals("empty"))
                            line = " ";

                        Score score = obj.getScore(replaceVariables(line, p));
                        score.setScore(scoreNumber);

                        scoreNumber++;
                    }

                    p.setScoreboard(scoreboard);
                }
            }
        }.runTaskTimer(m, 0L, 0L);
    }

    private static String replaceVariables(String inputString, Player p) {
        return inputString
                .replace("%money%", playertools.getMetadata(p,"money"))
                .replace("%faction%", playertools.getMetadata(p,"faction"))
                .replace("%class%", playertools.getMetadata(p,"class"))
                .replace("%bard_energy%", new SimpleDateFormat("s").format(new Date(playertools.getMetadata(p,"bardenergy")) + "s"))
                .replace("%spawntag%", new SimpleDateFormat("s").format(new Date((long) HCF_Timer.getCombatTime(p))) + "s")
                .replace("%ep_cd%", new SimpleDateFormat("s").format(new Date((long) HCF_Timer.getEpTime(p))) + "s")
                .replace("%location%", HCF_Claiming.sendFactionTerretory(p));
    }
}

// cs köcsög

/* Format:
 * Faction:                             5
 * Location:                           4/-
 * Money:                               3
 * Class:                               2
 * > additional information about class 1
 * */

            /*
                + BardEnergy
                + "§eSpawn Tag: §6%timer%",
                + Pearl cooldown
            */

//    add("§7┌─");
//    add("§7│ §eFaction: §6%faction%");
//    add("§7│ §eLocation: §6%location%");
//    add("§7│ §eMoney: §6$%money%");
//    add("§7│ §eClass: §6%class%");
//    add("§7└─");
//ArrayList<String> belowSb = new ArrayList<String>() {{
//    add("empty");
//    add("§7▍ §eSpawn Tag: §6%spawntag%");
//    add("§7▍ §ePearl: §6%ep_cd%");
//    add("§7▍ §eBard energy: §6%bard_energy%");
//}};

    private static Main m = Main.getPlugin(Main.class);

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
                    //ScoreboardManager manager = Bukkit.getScoreboardManager();

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
            if(line.contains("%spawntag%") && HCF_Timer.getCombatTime(p) <= 0.0) {
                continue;
            }
            else if(line.contains("%ep_cd%") && HCF_Timer.getEpTime(p) <= 0.0) {
                continue;
            }
//            else if(line.contains("%bard_energy%") && HCF_Timer.getEpTime(p) <= 0.0) {
//                continue;
//            }

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

    public static ArrayList<List<String>> sortLists() {
        List<String> str = ConfigLibrary.getScoreboard();
        List<String> fix = new ArrayList<>();
        List<String> timers = new ArrayList<>();
        for (String line : str) {
            if (line.contains("%spawntag%") || line.contains("%ep_cd%") || line.contains("%bard_energy%")) {
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

    private static String replaceVariables(String inputString, Player p) {
        return inputString
                .replace("%money%", playertools.getMetadata(p, "money"))
                .replace("%faction%", playertools.getMetadata(p, "faction"))
                .replace("%class%", playertools.getMetadata(p, "class"))
                //.replace("%bard_energy%", new SimpleDateFormat("s").format(new Date(playertools.getMetadata(p, "bardenergy")) + "s"))
                .replace("%bard_energy%", new SimpleDateFormat("s").format(new Date()))
                .replace("%spawntag%", new SimpleDateFormat("s").format(new Date((long) HCF_Timer.getCombatTime(p))) + "s")
                .replace("%ep_cd%", new SimpleDateFormat("s").format(new Date((long) HCF_Timer.getEpTime(p))) + "s")
                .replace("%location%", HCF_Claiming.sendFactionTerretory(p));
    }
}
