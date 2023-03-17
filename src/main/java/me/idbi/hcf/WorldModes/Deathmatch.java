package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class Deathmatch implements Gamemode {

    BukkitTask task = null;
    public void enable() {
        disable();
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());
        int EOTWTIME = Config.EOTWDuration.asInt() * 1000;
        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        HCF_Claiming.Faction_Claim spawnClaim = null;
        try {
            spawnClaim = Main.factionCache.get(1).getClaims().get(0);
        } catch (Exception ignored) {
            //Deathmatch
            Main.sendCmdMessage(Messages.cant_start_eotw.queue());
            return;
        }


        border.setSize(
                getDistanceBetweenPoints2D(
                        new HCF_Claiming.Point(spawnClaim.getStartX(), spawnClaim.getStartZ()),
                        new HCF_Claiming.Point(spawnClaim.getEndX(), spawnClaim.getEndZ())),
                EOTWTIME);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.eotw_start_title.language(p).queue(),
                    Messages.eotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            HCF_Timer.addEOTWTimer(p);
        }
        //Todo: EOTW Koth

        for(Faction f : Main.factionCache.values()) {
            if(f.getId() != 1) {
                f.setDTR(-9.9D);
                for (HCF_Claiming.Faction_Claim claim : f.getClaims()) {
                    claim.setAttribute(HCF_Claiming.ClaimAttributes.NORMAL);
                }
            }
        }
        Main.EOTWENABLED = true;
        //EOTW End routine
        new BukkitRunnable() {
            @Override
            public void run() {
                Main.EOTWENABLED = false;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                }
                Main.sendCmdMessage("EOTW ended!");
            }
        }.runTaskLater(Main.getPlugin(Main.class),Config.EOTWDuration.asInt() * 20L);
    }
    public void disable() {
        if(task != null) {
            Main.EOTWENABLED = false;
            World world = Bukkit.getWorld(Config.WorldName.asStr());
            Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));

            WorldBorder border = world.getWorldBorder();
            border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
            border.setSize(Config.WorldBorderSize.asInt());


            HCF_Claiming.Faction_Claim spawnClaim = null;
            try {
                spawnClaim = Main.factionCache.get(1).getClaims().get(0);
                spawnClaim.setAttribute(HCF_Claiming.ClaimAttributes.PROTECTED);
            } catch (Exception ignored) {
                return;
            }
            for(Faction f : Main.factionCache.values()) {
                f.refreshDTR();
            }
            for(Player p : Bukkit.getOnlinePlayers()) {
                HCF_Timer.removeEOTWTimer(p);
            }
            task.cancel();
            task = null;
        }
    }
}
