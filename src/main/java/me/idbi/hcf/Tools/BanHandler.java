package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;


import java.sql.Connection;
import java.util.UUID;

public class BanHandler {
    private static final Connection con = Main.getConnection();

    public static void banPlayerInHCF(HCFPlayer p) {
        SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'",p.getUUID().toString(), String.valueOf(System.currentTimeMillis() + (Main.deathbanTime)));
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
                SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", uuid.toString());
                return false;
            }
            return true;
        } else {
            if(p.isDeathBanned()) {
                Main.deathWaitClear.add(uuid);
                p.setDeathBanned(false);
                p.setDeathTime(0);
                SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", uuid.toString());
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
