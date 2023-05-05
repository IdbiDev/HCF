package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SOTW implements Gamemode {
    BukkitTask task = null;
    public void enable() {
        disable();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        //Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        long SOTWTime = Config.SOTWDuration.asInt() * 1000L;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        //border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        border.setCenter(Main.getSpawnLocation());
        Main.sendCmdMessage("&1&bSOTW STARTED Successfully!");
        Main.SOTWStarted = System.currentTimeMillis() + SOTWTime;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.sotw_start_title.language(p).queue(),
                    Messages.sotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            //Set SOTW Timer
            Timers.SOTW.add(p, SOTWTime);
        }
        Main.SOTWEnabled = true;
        //SOTW end routine
       task =  new BukkitRunnable() {
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
    public void disable(){
        if(task != null) {
            Main.SOTWEnabled = false;
            World world = Bukkit.getWorld(Config.WorldName.asStr());
            //Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));

            WorldBorder border = world.getWorldBorder();
            //border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
            border.setCenter(Main.getSpawnLocation());
            border.setSize(Config.WorldBorderSize.asInt());


            for(Faction f : Main.factionCache.values()) {
                f.refreshDTR();
            }
            for(Player p : Bukkit.getOnlinePlayers()) {
                Timers.SOTW.remove(p);
            }
            task.cancel();
            task = null;
        }
    }
}
