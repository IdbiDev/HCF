package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class Faction_Join {

    private static final Connection con = Main.getConnection("commands.Faction");

    public static void JoinToFaction(Player p, String factionname){
        if(playertools.getMetadata(p,"factionid").equals("0")){
            Integer id_faction = 0;
            for (Map.Entry<Integer, String> entry : Main.factionToname.entrySet()) {
                if(Objects.equals(entry.getValue(), factionname)){
                    id_faction = entry.getKey();
                    break;
                }
            }
            Main.Faction faction = Main.faction_cache.get(id_faction);
            if(faction.isPlayerInvited(p)){
                playertools.setMetadata(p,"factionid",id_faction);
                playertools.setMetadata(p,"faction",faction.factioname);
                SQL_Connection.dbExecute(con,"UPDATE members SET faction='?',factionname='?' WHERE uuid='?'", String.valueOf(id_faction),faction.factioname,p.getUniqueId().toString());

                faction.unInvitePlayer(p);

                //Sikeres belépés
                p.sendMessage(Messages.JOIN_MESSAGE.queue());

                //Faction -> xy belépett
                playertools.BroadcastFaction(faction.factioname, Messages.BC_JOIN_MESSAGE.repPlayer(p).queue());
            }else{
                //Nem vagy meghíva ebbe a facionbe
                p.sendMessage(Messages.NOT_INVITED.queue());
            }
        }else{
            // Már vagy egy factionbe
            p.sendMessage(Messages.YOU_ALREADY_IN_FACTION.queue());
        }
    }
}
