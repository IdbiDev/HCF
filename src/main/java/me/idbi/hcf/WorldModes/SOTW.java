package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SOTW {
    public static void EnableSOTW() {
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        long SOTWTime = Config.SOTWDuration.asInt() * 1000L;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        Main.sendCmdMessage("&1&bSOTW STARTED Successfully!");
        Main.SOTWStarted = System.currentTimeMillis() + SOTWTime;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.sotw_start_title.language(p).queue(),
                    Messages.sotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            //Set SOTW Timer
            HCF_Timer.addSOTWTimer(p,SOTWTime);
        }
        Main.SOTWEnabled = true;
        //SOTW end routine
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.SOTWEnabled = false;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                }
                Main.sendCmdMessage("SOTW ended!");
            }
        }.runTaskLater(Main.getPlugin(Main.class),Config.SOTWDuration.asInt() * 20L);
    }
}
