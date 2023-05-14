package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class EOTW implements Gamemode {
    BukkitTask task = null;
    @Override
    public void enable() {
        disable();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        //Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        int EOTWTIME = Config.EOTWDuration.asInt() * 1000;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(/*new Location(world, coords[0], coords[1], coords[2])*/Main.getSpawnLocation());

// köcsög
        border.setSize(1, EOTWTIME);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.eotw_start_title.language(p).queue(),
                    Messages.eotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            Timers.EOTW.add(p);
        }

        for(Faction f : Main.factionCache.values()) {
            for(Claiming.Faction_Claim c : f.getClaims()) {
                if(c.getAttribute().equals(Claiming.ClaimAttributes.NORMAL)) {
                    f.setDTR(-9.9D);
                }
            }
        }
        Main.EOTWENABLED = true;
        //EOTW End routine
        task = new BukkitRunnable() {
            @Override
            public void run() {
                Main.EOTWENABLED = false;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                }
                Koth.stopKoth();
            }
        }.runTaskLater(Main.getPlugin(Main.class),Config.EOTWDuration.asInt() * 20L);
    }
    @Override
    public void disable() {
        if(task != null) {
            Main.EOTWENABLED = false;
            Koth.stopKoth();
            World world = Bukkit.getWorld(Config.WorldName.asStr());
            //Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));

            WorldBorder border = world.getWorldBorder();
            border.setSize(Config.WorldBorderSize.asInt());

            border.setCenter(Main.getSpawnLocation());
            for(Faction f : Main.factionCache.values()) {
                f.refreshDTR();
            }
            for(Player p : Bukkit.getOnlinePlayers()) {
                Timers.EOTW.remove(p);
            }
            task.cancel();
            task = null;
        }
    }
}

