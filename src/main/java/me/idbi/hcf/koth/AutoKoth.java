package me.idbi.hcf.koth;

import me.idbi.hcf.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class AutoKoth {
    public static boolean isAutoKothEnabled = true;
    private static BukkitTask task;
    public void startAutoKoth() {
        if(isAutoKothEnabled && KOTH.GLOBAL_AREA == null){
            task = new BukkitRunnable(){
                @Override
                public void run() {
                    KOTH.StartRandomKoth();
                }
            }.runTaskTimer(Main.getPlugin(Main.class),20L,getRandomHour(1));
        }
    }
    public void StopAutoKoth(){
        isAutoKothEnabled = false;
        task.cancel();
    }
    private long getRandomHour(int interrvall) {
        return interrvall * 3600000L;
    }
    private long getRandomSec(int interrvall) {
        return interrvall * 60000L;
    }


}
