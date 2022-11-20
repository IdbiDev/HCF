package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.tools.DisplayName.displayTeams;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.Objects;

public class Faction_Leave {
    private static final Connection con = Main.getConnection("command.factions");

    public static void leave_faction(Player player) {
        if (!playertools.getMetadata(player, "factionid").equals("0")) {
            if (!Objects.equals(Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(player, "factionid"))).leader, player.getUniqueId().toString())) {

                SQL_Connection.dbExecute(con, "UPDATE members SET rank = '?', faction = '?',factionname='?' WHERE uuid = '?'", "None","0", "None", player.getUniqueId().toString());
                // Koba moment :3
                player.sendMessage(Messages.LEAVE_MESSAGE.queue());
                Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(player, "factionid")));
                playertools.setMetadata(player, "faction", "None");
                playertools.setMetadata(player, "factionid", "0");
                playertools.setMetadata(player, "rank", "None");

//                displayTeams.removePlayerFromTeam(player);
//                displayTeams.addToNonFaction(player);
                f.removePrefixPlayer(player);

                f.BroadcastFaction(Messages.BC_LEAVE_MESSAGE.repPlayer(player).queue());
                Scoreboards.refresh(player);
                f.refreshDTR();
            } else {
                //Todo: Factin leader is a fucking retarded bc he wanna leave the faction. Use /f disband
                player.sendMessage(Messages.LEADER_LEAVING_FACTION.queue());
            }
        } else {
            // Nem vagy tagja egy factionnak se
            player.sendMessage(Messages.NOT_IN_FACTION.queue());
        }

    }
}
