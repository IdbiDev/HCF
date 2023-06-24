package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.BanHandler;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.*;

public class PlayerPreJoin implements Listener {
    private static final Connection con = Playertools.con;

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {

        if (BanHandler.isPlayerBannedFromHCF(e.getUniqueId())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Messages.deathban_kick.setFormattedTime(BanHandler.getBanRemainingTime(e.getUniqueId())).language(HCFPlayer.getPlayer(e.getUniqueId())).queue());
        }
        try {
            if(Main.isUsingMongoDB()){
                ArrayList<Document> res = MongoDBDriver.FindAll("members", and(eq("uuid",e.getUniqueId().toString()),eq("name",e.getName())));
                if(res.size() > 0){
                    MongoDBDriver.Delete(MongoDBDriver.MongoCollections.MEMBERS,and(eq("uuid",e.getUniqueId().toString()),gt("_id",res.get(0))));
                }
            } else {
                HashMap<String,Object> map =  SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?' AND name='?' ORDER BY ID DESC",e.getUniqueId().toString(),e.getName());

                PreparedStatement ps = con.prepareStatement("DELETE FROM members WHERE uuid=? AND ID > ?");
                ps.setString(1, e.getUniqueId().toString());
                ps.setInt(2, (int) map.get("ID"));
                ps.executeUpdate();
            }
        } catch (Exception ex) {

        }
    }
}
