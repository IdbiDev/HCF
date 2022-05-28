package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class Faction_Leave {
    private static final Connection con = Main.getConnection("command.factions");

    public static void leave_faction(Player player){
        if (!playertools.getMetadata(player,"factionid").equals("0")){
            playertools.setMetadata(player,"faction","Nincs");
            playertools.setMetadata(player,"factionid","0");
            SQL_Connection.dbExecute(con,"UPDATE members SET faction = ?,factionname='?' WHERE uuid = '?'","0","Nincs",player.getUniqueId().toString());
            // Koba moment :3
            player.sendMessage(Messages.LEAVE_MESSAGE.queue());
            playertools.BroadcastFaction("§6" + Main.factionToname.get(Integer.parseInt(playertools.getMetadata(player,"factionid"))),Messages.BC_LEAVE_MESSAGE.repPlayer(player).queue());
        }else{
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.NOT_IN_FACTION.queue());
        }

    }
}
