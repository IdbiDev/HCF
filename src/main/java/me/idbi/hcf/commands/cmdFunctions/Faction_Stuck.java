package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Faction_Stuck {

    public static void processStuck(Player p){
        if(!HCF_Timer.checkStuckTimer(p)){
            //Todo: somethingwrite
            HCF_Timer.addStuckTimer(p);
            p.sendMessage(Messages.stuck_started.language(p).setAmount(String.valueOf(Main.stuck_duration)).queue());
            HCFPlayer player = HCFPlayer.getPlayer(p);
            player.setStuckLocation(new Location(
                    p.getWorld(),
                    p.getLocation().getBlockX(),
                    p.getLocation().getBlockY(),
                    p.getLocation().getBlockZ()));
        }
    }
}
