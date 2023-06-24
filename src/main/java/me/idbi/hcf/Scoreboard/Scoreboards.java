package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Scoreboards {
    private static final Main m = Main.getPlugin(Main.class);
    private static MiscTimers miscTimers = new MiscTimers();
    private static final DecimalFormat dfSharp = new DecimalFormat("0.0");

    public static void refresh(Player p) {
        if(HCFPlayer.getPlayer(p).getScoreboard() == null) {
            HCFPlayer.getPlayer(p).createScoreboard(p);
        }
        HCFPlayer.getPlayer(p).getScoreboard().update();
        TabManager.getManager().refresh(p);
        NameChanger.refresh(p);
    }

    public static void refreshAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            //Scoreboards.refresh(p);
            HCFPlayer.getPlayer(p).getScoreboard().update();
            NameChanger.refresh(p);
        }
    }

    public static String replaceVariables(String inputString, Player p) {
        HCFPlayer hcf = HCFPlayer.getPlayer(p);
        return inputString
                .replace("%money%", hcf.getMoney() + "")
                .replace("%faction%", hcf.getFactionName())
                .replace("%class%", Playertools.upperFirst(hcf.getPlayerClass().name()))
                .replace("%bard_energy%", dfSharp.format(
                                hcf.getBardEnergy())
                        .replace(",", "."))
                .replace("%spawntag%", Timers.COMBAT_TAG.getConvertSeconds(hcf))
                .replace("%pvp_timer%", Timers.PVP_TIMER.getConvertSeconds(hcf))
                .replace("%stuck%", Timers.STUCK.getConvertSeconds(hcf))
                .replace("%logout%", Timers.LOGOUT.getConvertSeconds(hcf))
                .replace("%enderpearl%", Timers.ENDER_PEARL.getRoundSeconds(hcf) + "s")
                .replace("%apple%", Timers.APPLE.getConvertSeconds(hcf))
                .replace("%home%", Timers.HOME.getConvertSeconds(hcf))
                .replace("%bard_cooldown%", Timers.BARD_COOLDOWN.getRoundSeconds(hcf) + "s")
                .replace("%gapple%", Timers.GAPPLE.getConvertSeconds(hcf))
                .replace("%eotw%", Timers.EOTW.getConvertSeconds(hcf))
                .replace("%sotw%", Timers.SOTW.getConvertSeconds(hcf))
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
