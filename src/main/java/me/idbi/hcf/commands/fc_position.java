package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.UUID;

import static me.idbi.hcf.tools.playertools.createCustomFaction;

public class fc_position implements CommandExecutor {

    private static final Connection con = Main.getConnection("me");
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player p){
            if(playertools.getMetadata(p,"factionid").equals("0")){
                p.sendMessage(Messages.NOT_IN_FACTION.queue());
                return false;
            }
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
           //faction.BroadcastFaction("&2"+p.getName()+" >> &o" +  convertLocation(p,p.getLocation()));

            faction.BroadcastFaction(convertLocation(p, p.getLocation()));
        }
        return false;
    }

    public static String convertLocation(Player p, Location loc) {
        return Messages.FACTION_PLAYER_POSITION.repPlayer(p).repCoords(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).queue();
    }

    public static void lecgo(){
        for(int i=0;i<=1000;i++){
            int x = createCustomFaction(i+"meow",UUID.randomUUID().toString());

            for(int r=0;r<=100;r++)
                SQL_Connection.dbExecute(con,"INSERT INTO ranks SET name='?',faction='?'",r+"rank", String.valueOf(x));

            for(int c=2;c<=100;c++){
                int lvl = (int) (Math.random() * (9999 - 1 + 1) + 1);
                SQL_Connection.dbExecute(con,"INSERT INTO claims SET factionid='?'," +
                        "startX='?'," +
                        "startZ='?'," +
                        "endX='?'," +
                        "endZ='?'," +
                        "type='normal'", String.valueOf(x), String.valueOf(lvl),String.valueOf(lvl),String.valueOf(lvl),String.valueOf(lvl));
            }
        }
    }
}
