package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import org.bukkit.Bukkit;

public class Tasks {

    public static void executeScheduled(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskTimer(Main.getInstance(), runnable, 0L, HCF);
    }

    public static void executeLater(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), runnable, HCF);
    }

    public static void executeAsync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
    }

    public static void executeScheduledAsync(int HCF, Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), runnable, 0L, HCF);
    }

    public static void execute(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(Main.getInstance(), runnable);
    }
}
