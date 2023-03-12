package me.idbi.hcf.Commands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Map;

public class FactionPositionCommand implements CommandExecutor {

    private static final Connection con = Main.getConnection();

    public static String convertLocation(Player p, Location loc) {
        return Messages.faction_player_position.language(p).setPlayer(p).setCoords(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).queue();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if(p.hasPermission("factions.commands.factioncall")) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return false;
            }
            if (Playertools.getPlayerFaction(p) == null) {
                p.sendMessage(Messages.not_in_faction.language(p).queue());
                return false;
            }
            Faction faction = Playertools.getPlayerFaction(p);
            //faction.BroadcastFaction("&2"+p.getName()+" >> &o" +  convertLocation(p,p.getLocation()));

            for (Player member : faction.getMembers()) {
                member.sendMessage(convertLocation(p, p.getLocation()));
            }
            for (HCFPlayer hcf : Main.playerCache.values()) {
                hcf.save();
            }
            for (Map.Entry<Integer, Faction> integerFactionEntry : Main.factionCache.entrySet()) {
                integerFactionEntry.getValue().saveFactionData();
                for (FactionRankManager.Rank rank : integerFactionEntry.getValue().ranks) {
                    rank.saveRank();
                }
            }
        }
        return false;
    }
    // xd xDDDDDDD
}
