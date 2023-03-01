package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class SOTW {
    public static void EnableSOTW() {
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        int SOTWTime = Config.SOTWDuration.asInt() * 1000;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        /*HCF_Claiming.Faction_Claim spawnClaim = null;
        try{
            spawnClaim = Main.faction_cache.get(1).claims.get(0);
        }catch (IndexOutOfBoundsException ignored){
            Main.sendCmdMessage("&4&bEOTW START FAILED!&f\n&4Spawn does not exists!");
            return;
        }*/
        Main.sendCmdMessage("&1&bSOTW STARTED Successfully!");
        Main.SOTWStarted = System.currentTimeMillis() + SOTWTime;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(
                    Messages.sotw_start_title.language(p).queue(),
                    Messages.sotw_start_subtitle.language(p).queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
        }
        Main.SOTWEnabled = true;
    }
}
