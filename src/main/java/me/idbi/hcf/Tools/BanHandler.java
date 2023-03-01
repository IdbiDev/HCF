package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class BanHandler {
    private static final Connection con = Main.getConnection();

    public static void banPlayerInHCF(Player player) {
        SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", player.getUniqueId().toString(), String.valueOf(System.currentTimeMillis() + (Main.deathbanTime * 60000L)));
        player.kickPlayer(Messages.deathban_kick.queue());

    }

    public static void banPlayerInHCF(OfflinePlayer player) {
        SQL_Connection.dbExecute(con, "INSERT INTO deathbans SET uuid='?',time='?'", player.getUniqueId().toString(), String.valueOf(System.currentTimeMillis() + (Main.deathbanTime * 60000L)));
    }

    public static boolean isPlayerBannedFromHCF(UUID uuid) {
        HashMap<String, Object> ban = SQL_Connection.dbPoll(con, "SELECT * FROM deathbans WHERE uuid='?'", uuid.toString());
        if (!ban.isEmpty()) {
            if (Long.parseLong(String.valueOf(ban.get("time"))) <= System.currentTimeMillis()) {
                SQL_Connection.dbExecute(con, "DELETE FROM deathbans WHERE uuid='?'", (String) ban.get("uuid"));
                Main.deathWaitClear.add(uuid);

                return false;
            }
            return true;
        }
        return false;
    }

    public static int getBanRemainingTime(UUID uuid) {
        HashMap<String, Object> ban = SQL_Connection.dbPoll(con, "SELECT * FROM deathbans WHERE uuid='?'", uuid.toString());
        if (!ban.isEmpty()) {
            return Math.toIntExact((Long.parseLong(String.valueOf(ban.get("time"))) - System.currentTimeMillis()) / 60000);
        }
        return 0;
    }
}
