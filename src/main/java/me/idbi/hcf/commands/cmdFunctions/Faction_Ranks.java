package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.entity.Player;

public class Faction_Ranks {
    public static void addRank(Player p, String name){
        if(!playertools.getMetadata(p,"factionid").equals("0")){
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
            //Todo: Playertools permission check function
            // ToDo: Check rank exists
            if(true){
                rankManager.CreateNewRank(faction,name);
                p.sendMessage(Messages.FACTION_CREATE_RANK.queue());
            }else{
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        }else{
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
}
