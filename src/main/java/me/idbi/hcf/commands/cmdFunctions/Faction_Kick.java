package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;

public class Faction_Kick {
    public static Connection con = Main.getConnection("cmd.FactionCreate");
    public static void kick_faction(Player player,String target) {
        if (!playertools.getMetadata(player, "factionid").equals("0")) {
            if (playertools.hasPermission(player, rankManager.Permissions.KICK)) {
                OfflinePlayer targetPlayer_Offline = Bukkit.getOfflinePlayer(target);
                Player targetPlayer_Online;
                //Todo: Kick message
                if(targetPlayer_Offline.isOnline()) {
                    targetPlayer_Online = targetPlayer_Offline.getPlayer();
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?' faction = '?',factionname='?' WHERE uuid = '?'", "none","0", "Nincs", targetPlayer_Online.getUniqueId().toString());
                    Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(targetPlayer_Online, "factionid")));
                    playertools.setMetadata(targetPlayer_Online, "faction", "Nincs");
                    playertools.setMetadata(targetPlayer_Online, "factionid", "0");
                    f.BroadcastFaction(Messages.BC_LEAVE_MESSAGE.repPlayer(targetPlayer_Online).queue());
                    Scoreboards.refresh(targetPlayer_Online);
                    f.refreshDTR();
                }else {
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?' faction = '?',factionname='?' WHERE uuid = '?'", "none","0", "Nincs", targetPlayer_Offline.getUniqueId().toString());
                    Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(player, "factionid")));
                    f.refreshDTR();
                }
                player.sendMessage(Messages.NOT_A_NUMBER.queue());
            } else {
                player.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.NOT_IN_FACTION.queue());
        }

    }
}
