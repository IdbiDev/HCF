package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Claiming;
import me.idbi.hcf.tools.playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;

import static me.idbi.hcf.tools.playertools.getDistanceBetweenPoints2D;

public class EOTW {

    public static void EnableEOTW() {
        World world = Bukkit.getWorld(Config.world_name.asStr());
        Integer[] coords = playertools.getInts(Config.spawn_location.asStr().split(" "));
        int EOTWTIME = Config.eotw_length.asInt() * 1000;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.world_border_size.asInt());

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        HCF_Claiming.Faction_Claim spawnClaim = null;
        try{
            spawnClaim = Main.faction_cache.get(1).claims.get(0);
        }catch (Exception ignored){
            Main.sendCmdMessage(Messages.cant_start_eotw.queue());
            return;
        }


        border.setSize(
                getDistanceBetweenPoints2D(
                        new HCF_Claiming.Point(spawnClaim.startX,spawnClaim.startZ),
                        new HCF_Claiming.Point(spawnClaim.endX,spawnClaim.endZ)),
                EOTWTIME);

        Main.EOTWStarted = System.currentTimeMillis() + EOTWTIME;


        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(
                    Messages.eotw_start_title.language(p).queue(),
                    Messages.eotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL,1f,1f);
        }
        Main.EOTW_ENABLED = true;

    }
}
