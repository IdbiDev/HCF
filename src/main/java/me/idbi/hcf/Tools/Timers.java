package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public enum Timers {

    COMBAT_TAG(Config.CombatTag.asInt() * 1000),
    STUCK(Config.StuckTimer.asInt() * 1000),
    APPLE(Config.GolderApple.asInt() * 1000),
    GAPPLE(Config.EnchantedGoldenApple.asInt() * 1000),
    PVP_TIMER(Config.PvPTimer.asInt() * 1000),
    CLASS_WARMUP(Config.ClassWarmupTime.asInt() * 1000),
    BARD_COOLDOWN(Config.BardEnergy.asInt() * 1000),
    ARCHER_TAG(Config.ArcherTag.asInt() * 1000),
    ENDER_PEARL(Config.EnderPearl.asInt() * 1000),
    LOGOUT(Config.Logout.asInt() * 1000),
    HOME(Config.HomeTimer.asInt() * 1000),
    SOTW(Config.SOTWDuration.asInt() * 1000),
    EOTW(Config.EOTWDuration.asInt() * 1000);

    private final int time;

    Timers(int time) {
        this.time = time;
    }

    public void add(Player p) {
        add(HCFPlayer.getPlayer(p));
    }

    public void add(Player p, long time) {
        add(HCFPlayer.getPlayer(p), time);
    }
    public void add(UUID uuid) {
        add(HCFPlayer.getPlayer(uuid));
    }
    public void add(HCFPlayer hcfPlayer) {
        hcfPlayer.addTimer(this, this.time + System.currentTimeMillis());
    }
    public void add(HCFPlayer hcfPlayer, long time) {
        hcfPlayer.addTimer(this, time);
    }

    public boolean canAdd(HCFPlayer hcfPlayer) {
        if(has(hcfPlayer)) {
            return false;
        } else {
            add(hcfPlayer);
            return true;
        }
    }

    public boolean canAdd(Player player) {
        return canAdd(HCFPlayer.getPlayer(player));
    }

    public void remove(Player p) {
        remove(HCFPlayer.getPlayer(p));
    }
    public void remove(HCFPlayer hcfPlayer) {
        hcfPlayer.removeTimer(this);
    }
    public void remove(UUID uuid) {
        remove(HCFPlayer.getPlayer(uuid));
    }

    public boolean has(Player p) {
        return has(HCFPlayer.getPlayer(p));
    }

    public boolean has(UUID uuid) {
        return has(HCFPlayer.getPlayer(uuid));
    }

    public boolean has(HCFPlayer hcfPlayer) {
        if(hcfPlayer.getTimers().get(this) == null) return false;

        long unixTime = System.currentTimeMillis();
        long timer = hcfPlayer.getTimers().get(this);
        if(timer >= unixTime) {
            return true;
        } else {
            remove(hcfPlayer);
            return false;
        }
    }

    public boolean nowExpire(Player p) {
        return nowExpire(HCFPlayer.getPlayer(p));
    }

    public boolean nowExpire(HCFPlayer hcfPlayer) {
        if(hcfPlayer.getTimers().get(this) == null) return false;

        long unixTime = System.currentTimeMillis();
        long timer = hcfPlayer.getTimers().get(this);

        if(timer - unixTime <= 0) {
            remove(hcfPlayer);
            return true;
        }
        return false;
    }

    public int getSeconds(HCFPlayer hcfPlayer) {
        return Math.toIntExact(get(hcfPlayer)) / 1000;
    }

    /**
     *
     * @param hcfPlayer
     * @return Format: "12,3"
     */
    public String getRoundSeconds(HCFPlayer hcfPlayer) {
        long timer = get(hcfPlayer);

        return String.format("%.1f", timer / 1000.0).replace(",", ".");
    }

    public long get(HCFPlayer hcfPlayer) {
        long currentUnix = System.currentTimeMillis();
        if(hcfPlayer.getTimers().get(this) == null) {
            /*if(this == ENDER_PEARL) {
                System.out.println("IGEN");
            }*/
            return 0;
        }
        long timer = hcfPlayer.getTimers().get(this);
        long diff = timer - currentUnix;
        if(diff <= 0) {
            //System.out.println("Removeolta");
            remove(hcfPlayer);
        }
        return timer - currentUnix;
    }

    public long get(Player player) {
        return get(HCFPlayer.getPlayer(player));
    }

    public String getConvertSeconds(HCFPlayer hcfPlayer) {
        return convertTime(getSeconds(hcfPlayer));
    }

    public static String convertTime(int seconds) {
        int minute = 0;
        int hours = 0;
        int second = seconds;
        if (seconds > 60) {
            second = seconds % 60;
            minute = (seconds / 60) % 60;
            hours = (seconds / 60) / 60;
        }

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
