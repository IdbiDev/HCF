package me.idbi.hcf.Scoreboard;

import me.idbi.hcf.CustomFiles.Configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class ScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective ;
    private       int        index     ;

    public ScoreboardBuilder(Scoreboard scoreboard, Objective objective, int index) {
        this.scoreboard = scoreboard;
        this.objective  = objective ;
        this.index      = index     ;
    }

    public ScoreboardBuilder(org.bukkit.scoreboard.Scoreboard scoreboard, Objective objective) {
        this(scoreboard, objective, getMinValue(objective));
    }

    public ScoreboardBuilder(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        Objective objective  = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            this.index     = 15;
            this.objective = scoreboard.registerNewObjective("board","dummy");
        } else {
            this.objective = objective;
            this.index     = getMinValue(objective);
        }
    }

    public ScoreboardBuilder(Scoreboard scoreboard, int index) {
        this.scoreboard = scoreboard;
        Objective objective  = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            this.index     = index;
            this.objective = scoreboard.registerNewObjective("board","dummy");
        } else {
            this.objective = objective;
            this.index     = index    ;
        }
    }

    public ScoreboardBuilder() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective  = scoreboard.registerNewObjective("board","dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.index = 15;
    }

    public ScoreboardBuilder nextLine(String s) {
        Score score = objective.getScore(s);
        score.setScore(index--);
        return this;
    }

    public ScoreboardBuilder removeLine(int line) {
        for (String entry : scoreboard.getEntries()) if (objective.getScore(entry).getScore() == line) scoreboard.resetScores(entry);
        return this;
    }

    public ScoreboardBuilder removeLines() {
        for (String entry : scoreboard.getEntries())
            scoreboard.resetScores(entry);
        return this;
    }

    public ScoreboardBuilder removeLine(int line, String s) {
        for (String entry : scoreboard.getEntries())
            if (!entry.equals(s) && objective.getScore(entry).getScore() == line)
                scoreboard.resetScores(entry);
        return this;
    }
    public ScoreboardBuilder setLine(int line, String s) {
        if (objective.getScore(s).getScore() == line) return this; // If the string already there It will take less resource from the server
        Score score = objective.getScore(s);
        score.setScore(line);
        removeLine(line, s);
        return this;
    }

    public ScoreboardBuilder setDisplayName(String s) {
        objective.setDisplayName(s);
        return this;
    }

    public Scoreboard build() {
        return scoreboard;
    }

    // Utils

    public static ScoreboardBuilder getOrCreate(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) return new ScoreboardBuilder();
        return new ScoreboardBuilder(scoreboard, 0);
    }

    public static Objective getObjective(Scoreboard scoreboard) {
        if (scoreboard.getObjective(DisplaySlot.SIDEBAR) == null) return null;
        return scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    public static int getMaxValue(Objective objective) {
        if (objective == null) return 0;
        int index = 0;

        Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard == null) return 0;

        for (String key : scoreboard.getEntries()) {
            Score score = objective.getScore(key);
            if (index < score.getScore()) index = score.getScore();
        }

        return index;
    }

    public static int getMinValue(Objective objective) {
        if (objective == null) return 0;
        int index = 0;

        Scoreboard scoreboard = objective.getScoreboard();
        if (scoreboard == null) return 0;

        for (String key : scoreboard.getEntries()) {
            Score score = objective.getScore(key);
            if (index > score.getScore()) index = score.getScore();
        }

        return index;
    }
}
