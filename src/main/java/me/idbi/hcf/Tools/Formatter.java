package me.idbi.hcf.Tools;

import java.text.*;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import org.apache.commons.lang.time.*;
import java.util.concurrent.*;
import java.util.*;

public class Formatter {

    private static final DecimalFormat HEALTH_FORMATTER;
    private static final Map<Long, String> ENERGY_CACHE;
    private static final long MINUTE;
    private static final Map<Double, String> DTR_CACHE;
    private static final long HOUR;
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING;
    private static final DecimalFormat BARD_ENERGY_FORMATTER;
    private static final DecimalFormat DTR_FORMATTER;
    private static final ThreadLocal<DecimalFormat> REMAINING_SECONDS;
    public static SimpleDateFormat DATE_FORMAT;

    public static String formatDtr(Faction faction) {
        double dtr = faction.getDTR();
        DecimalFormat formatter = new DecimalFormat("0.0");

        double lowDTR = Config.DTRLowDTR.asDouble();
        String color = Config.DTRColorNormal.asStr();

        if(dtr <= lowDTR && dtr > 0) {
            color = Config.DTRColorLowDTR.asStr();
        } else if(dtr <= 0) {
            color = Config.DTRColorRaidable.asStr();
        }

        String suffix = Config.DTRSymbolNormal.asStr();
        if(faction.getDTR_TIMEOUT() != 0) {
            suffix = Config.DTRSymbolRegenerating.asStr();
        }

        return color + String.format("#.1f", dtr) + suffix;
    }

    /**
     *
     * @param timer
     * @return `HH:`mm:ss
     */
    public static String formatMMSS(long timer) {
        return DurationFormatUtils.formatDuration(timer, ((timer >= Formatter.HOUR) ? "HH:" : "") + "mm:ss");
    }

    /**
     *
     * @param timer
     * @param format
     * @param remaining
     * @return `HH:`mm:ss
     */
    public static String getRemaining(long timer, boolean format, boolean remaining) {
        if (format && timer < Formatter.MINUTE) {
            return (remaining ? Formatter.REMAINING_SECONDS_TRAILING : Formatter.REMAINING_SECONDS).get().format(timer * 0.001) + 's';
        }
        return DurationFormatUtils.formatDuration(timer, ((timer >= Formatter.HOUR) ? "HH:" : "") + "mm:ss");
    }

    static {
        DTR_FORMATTER = new DecimalFormat("0.0");
        HEALTH_FORMATTER = new DecimalFormat("#.#");
        BARD_ENERGY_FORMATTER = new DecimalFormat("0.0");
        MINUTE = TimeUnit.MINUTES.toMillis(1L);
        HOUR = TimeUnit.HOURS.toMillis(1L);
        REMAINING_SECONDS = ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));
        REMAINING_SECONDS_TRAILING = ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));
        DTR_CACHE = new HashMap<>();
        ENERGY_CACHE = new HashMap<>();
        DATE_FORMAT = new SimpleDateFormat("dd MMMM hh:mm");
    }

    public static Long parse(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        if (input.equals("0")) {
            return 0L;
        }
        long l = 0L;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char chr = input.charAt(i);
            if (Character.isDigit(chr)) {
                builder.append(chr);
            }
            else {
                String s;
                if (Character.isLetter(chr) && !(s = String.valueOf(builder)).isEmpty()) {
                    l += convert(Integer.parseInt(s), chr);
                    builder = new StringBuilder();
                }
            }
        }
        return (l == 0L || l == -1L) ? null : l;
    }

    private static long convert(int time, char timers) {
        switch (timers) {
            case 'y': {
                return time * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return time * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return time * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return time * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return time * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return time * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }

    public static String getRemaining(long time, boolean b) {
        return getRemaining(time, b, true);
    }

    public static String formatDetailed(long time) {
        return DurationFormatUtils.formatDurationWords(time, true, true);
    }

    public static String formatBardEnergy(double time) {
        /*String s = Formatter.ENERGY_CACHE.get(time);
        if (s != null) {
            return s;
        }*/
        String ss = Formatter.BARD_ENERGY_FORMATTER.format(time);
        //Formatter.ENERGY_CACHE.put(time, ss);
        return ss;
    }

    public static String formatHealth(double input) {
        return Formatter.HEALTH_FORMATTER.format(input);
    }
}
