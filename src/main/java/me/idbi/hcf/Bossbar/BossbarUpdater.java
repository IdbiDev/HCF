package me.idbi.hcf.Bossbar;

import me.idbi.hcf.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class BossbarUpdater extends BukkitRunnable {
    private Main bossbar;

    public BossbarUpdater(Main bossbar) {
        this.bossbar = bossbar;
    }

    public void run() {
        for (Bossbar b : Bossbar.bars.values())
            continue;
            //b.update();
    }
}
