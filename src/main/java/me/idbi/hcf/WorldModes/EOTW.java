package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;

import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class EOTW {

    public static void EnableEOTW() {
        World world = Bukkit.getWorld(ConfigLibrary.World_name.getValue());
        Integer[] coords = playertools.getInts(ConfigLibrary.Spawn_location.getValue().split(" "));
        int timeInSeconds = Integer.parseInt(ConfigLibrary.EOTW_time.getValue()) * 60;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Integer.parseInt(ConfigLibrary.World_width.getValue()));

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        HCF_Claiming.Faction_Claim spawnClaim = null;
        try{
            spawnClaim = Main.faction_cache.get(1).claims.get(0);
        }catch (Exception ignored){
            Main.sendCmdMessage(Messages.CANT_START_EOTW.queue());
            return;
        }


        border.setSize(
                getDistanceBetweenPoints2D(
                        new HCF_Claiming.Point(spawnClaim.startX,spawnClaim.startZ),
                        new HCF_Claiming.Point(spawnClaim.endX,spawnClaim.endZ)),
                timeInSeconds);

        Main.EOTWStarted = System.currentTimeMillis() + (timeInSeconds * 1000L);


        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(
                    Messages.EOTW_START_TITLE.queue(),
                    Messages.EOTW_START_SUBTITLE.queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL,1f,1f);
        }
        Main.EOTW_ENABLED = true;

    }
}
