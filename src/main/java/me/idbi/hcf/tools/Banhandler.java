package me.idbi.hcf.tools;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class Banhandler {
    private static final Connection con = Main.getConnection("BanHandler");

    public static void banPlayerInHCF(Player player){
        SQL_Connection.dbExecute(con,"INSERT INTO deathbans SET uuid='?',time='?'", player.getUniqueId().toString(), String.valueOf(System.currentTimeMillis()+(Main.death_time*60000)));
        player.kickPlayer("§4HCF - Deathban\nMeghaltál, ezért §f" + Main.death_time+"§4 percre\n ki vagy tiltva a szerverről!");
    }

    public static boolean isPlayerBannedFromHCF(UUID uuid){
        HashMap<String, Object> ban = SQL_Connection.dbPoll(con,"SELECT * FROM deathbans WHERE uuid='?'",uuid.toString());
        if(!ban.isEmpty()){
            if(Long.parseLong(String.valueOf(ban.get("time"))) <= System.currentTimeMillis()){
                SQL_Connection.dbExecute(con,"DELETE FROM deathbans WHERE uuid='?'",(String)ban.get("uuid"));
                return false;
            }
            return true;
        }
        return false;
    }

    public static int getBanRemainingTime(UUID uuid){
        HashMap<String, Object> ban = SQL_Connection.dbPoll(con,"SELECT * FROM deathbans WHERE uuid='?'", uuid.toString());
        if(!ban.isEmpty()){
            return Math.toIntExact((Long.parseLong(String.valueOf(ban.get("time"))) - System.currentTimeMillis()) / 60000);
        }
        return 0;
    }
}
