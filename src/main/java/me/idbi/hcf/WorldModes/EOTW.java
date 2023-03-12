package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class EOTW {

    public static void EnableEOTW() {
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        int EOTWTIME = Config.EOTWDuration.asInt() * 1000;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        HCF_Claiming.Faction_Claim spawnClaim = null;
        try {
            spawnClaim = Main.factionCache.get(1).claims.get(0);
        } catch (Exception ignored) {
            Main.sendCmdMessage(Messages.cant_start_eotw.queue());
            return;
        }


        border.setSize(
                getDistanceBetweenPoints2D(
                        new HCF_Claiming.Point(spawnClaim.startX, spawnClaim.startZ),
                        new HCF_Claiming.Point(spawnClaim.endX, spawnClaim.endZ)),
                EOTWTIME);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.eotw_start_title.language(p).queue(),
                    Messages.eotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
        }
        //Todo: EOTW Koth
        for(Faction f : Main.factionCache){

        }
        Main.EOTWENABLED = true;

    }
}
