package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.BanHandler;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static me.idbi.hcf.Commands.FactionCommands.FactionCreateCommand.con;

public class PlayerPreJoin implements Listener {

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
        if (BanHandler.isPlayerBannedFromHCF(e.getUniqueId())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Messages.deathban_kick.setFormattedTime(BanHandler.getBanRemainingTime(e.getUniqueId())).language(HCFPlayer.getPlayer(e.getUniqueId())).queue());
        }
        try {
        HashMap<String,Object> map =  SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?' AND name='?' ORDER BY ID DESC",e.getUniqueId().toString(),e.getName());
        // 15 , 29 ,69

            PreparedStatement ps = con.prepareStatement("DELETE FROM members WHERE uuid=? AND ID > ?");
            ps.setString(1, e.getUniqueId().toString());
            ps.setInt(2, (int) map.get("ID"));
            ps.executeUpdate();
        } catch (Exception ex) {

        }
    }
}
