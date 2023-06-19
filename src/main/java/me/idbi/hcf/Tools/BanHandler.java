package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Connection;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class BanHandler {
    private static final Connection con = Playertools.con;

    public static void banPlayerInHCF(HCFPlayer p) {
        if(Main.isUsingMongoDB()){
            Document insert = new Document();
            insert.append("uuid",p.getUUID().toString());
            insert.append("time",System.currentTimeMillis() + (Main.deathbanTime));
            MongoDBDriver.Insert(MongoDBDriver.MongoCollections.DEATHBANS,insert);
        } else {
            SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'",p.getUUID().toString(), String.valueOf(System.currentTimeMillis() + (Main.deathbanTime)));
        }
        p.setDeathBanned(true);
        p.setDeathTime(System.currentTimeMillis() + (Main.deathbanTime));
    }

    public static boolean isPlayerBannedFromHCF(UUID uuid) {
        HCFPlayer p = HCFPlayer.getPlayer(uuid);
        if(p.getDeathTime() >= System.currentTimeMillis()) {
            if(p.getLives() > 0){
                p.setLives(p.getLives()-1);
                Main.deathWaitClear.add(uuid);
                p.setDeathBanned(false);
                p.setDeathTime(0);
                if(Main.isUsingMongoDB()){
                    Bson where = eq("uuid",uuid.toString());
                    MongoDBDriver.Delete(MongoDBDriver.MongoCollections.DEATHBANS,where);
                } else {
                    SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", uuid.toString());
                }
                return false;
            }
            return true;
        } else {
            if(p.isDeathBanned()) {
                Main.deathWaitClear.add(uuid);
                p.setDeathBanned(false);
                p.setDeathTime(0);
                if(Main.isUsingMongoDB()){
                    Bson where = eq("uuid",uuid.toString());
                    MongoDBDriver.Delete(MongoDBDriver.MongoCollections.DEATHBANS,where);
                } else {
                    SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", uuid.toString());
                }
                return false;
            }
            return false;
        }
    }

    public static int getBanRemainingTime(UUID uuid) {
        HCFPlayer p = HCFPlayer.getPlayer(uuid);
        return Math.toIntExact((((p.getDeathTime() - System.currentTimeMillis()) / 1000)));
    }
}
