package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Faction_Stuck {

    public static void processStuck(Player p){
        if(!HCF_Timer.checkStuckTimer(p)){
            //Todo: somethingwrite
            HCF_Timer.addStuckTimer(p);
            p.sendMessage(Messages.STUCK_STARTED.setAmount(String.valueOf(Main.stuck_duration)).queue());
            playertools.setMetadata(p,"stuck_loc",new Location(p.getWorld(),p.getLocation().getBlockX(),p.getLocation().getBlockY(),p.getLocation().getBlockZ()));
        }
    }
}
