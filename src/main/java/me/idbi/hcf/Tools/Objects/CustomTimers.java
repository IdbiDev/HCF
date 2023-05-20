package me.idbi.hcf.Tools.Objects;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Scoreboard.Scoreboards;

import java.util.Map;

public class CustomTimers {

    public String name;
    public long time;
    public String text;

    public CustomTimers(String name, long time, String text) {
        this.name = name;
        this.time = time;
        this.text = text;
        Main.customSBTimers.put(name, this);
        AdminScoreboard.RefreshAll();
        Scoreboards.refreshAll();
    }

    public static void refreshAll() {
        for (Map.Entry<String, CustomTimers> customSBTimer : Main.customSBTimers.entrySet()) {
            if (!customSBTimer.getValue().isActive())
                customSBTimer.getValue().delete();
        }
    }

    public static boolean isCreated(String name) {
        for (Map.Entry<String, CustomTimers> customSBTimer : Main.customSBTimers.entrySet()) {
            if (customSBTimer.getKey().equals(name)) return true;
        }

        return false;
    }

    public long getTime() {
        return time - System.currentTimeMillis();
    }

    public void setTime(long time) {
        this.time = time;
        Main.customSBTimers.put(this.name, this);
        refreshScoreboards();
    }

    public boolean isActive() {
        if (getTime() <= 0) {
            Main.customSBTimers.remove(this.name);
            return false;
        }
        return true;
    }

    public void setText(String text) {
        this.text = text;
        Main.customSBTimers.put(this.name, this);
        refreshScoreboards();
    }

    public String getFormatted() {
        String timeFormatted = Scoreboards.ConvertTime(((int) (getTime() / 1000)));
        return text + " " + timeFormatted;
    }

    public void delete() {
        Main.customSBTimers.remove(this.name);
        refreshScoreboards();
    }

    public void refreshScoreboards() {
        Scoreboards.refreshAll();
        AdminScoreboard.RefreshAll();
    }
}
