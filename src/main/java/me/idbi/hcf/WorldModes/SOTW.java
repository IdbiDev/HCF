package me.idbi.hcf.WorldModes;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class SOTW {
    public static void EnableSOTW() {
        World world = Bukkit.getWorld(ConfigLibrary.World_name.getValue());
        Integer[] coords = playertools.getInts(ConfigLibrary.Spawn_location.getValue().split(" "));
        int timeInSeconds = Integer.parseInt(ConfigLibrary.SOTW_TIME.getValue()) * 60;

        WorldBorder border = world.getWorldBorder();
        border.setSize(Integer.parseInt(ConfigLibrary.World_width.getValue()));

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        /*HCF_Claiming.Faction_Claim spawnClaim = null;
        try{
            spawnClaim = Main.faction_cache.get(1).claims.get(0);
        }catch (IndexOutOfBoundsException ignored){
            Main.sendCmdMessage("&4&bEOTW START FAILED!&f\n&4Spawn does not exists!");
            return;
        }*/
        Main.sendCmdMessage("&1&bSOTW STARTED Successfully!");
        Main.SOTWSTARTED = System.currentTimeMillis() + SOTWTime;
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle(
                    Messages.SOTW_START_TITLE.queue(),
                    Messages.SOTW_START_SUBTITLE.queue()
            );
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL,1f,1f);
        }
        Main.SOTW_ENABLED = true;
    }
}
