package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class fc_position implements CommandExecutor {
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
}
