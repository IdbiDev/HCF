package me.idbi.hcf.Tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;


public class FactionRankManager {
    private static final Connection con = Main.getConnection();

    public static Rank create(Player player, String name, boolean isLeader, boolean isDefault) {
        Faction faction = Playertools.getPlayerFaction(player);
        assert faction != null;
        for (FactionRankManager.Rank rank : faction.ranks)
            if (rank.name.equalsIgnoreCase(name))
                return null;

        int id = Playertools.getFreeRankId();
        Rank rank = new Rank(id, name, isDefault, isLeader);

        if (isLeader)
            rank.priority = 9999;
        else if (isDefault)
            rank.priority = -9999;
        else
            rank.priority = -9999 + faction.ranks.size();

        Main.ranks.add(rank);
        faction.ranks.add(rank);
        Scoreboards.RefreshAll();
        SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?', isDefault='?',isLeader='?',priority='?', ID='?'",
                name, String.valueOf(faction.id),
                (rank.isDefault) ? "1" : "0",
                (rank.isLeader) ? "1" : "0",
                rank.priority + "",
                String.valueOf(id));
        //rank.saveRank();
        return rank;
    }
    // public static Rank create(Faction faction, String name) {
//        for (Faction_Rank_Manager.Rank rank : faction.ranks)
//            if (rank.name.equalsIgnoreCase(name))
//                return null;
//
//
//        int id = SQL_Connection.dbSyncExec(con, "INSERT INTO ranks SET name = '?', faction='?'", name, String.valueOf(faction.id));
//        Rank rank = new Rank(id, name);
//        faction.ranks.add(rank);
//
//        rank.priority = -9999 + faction.ranks.size();
//        rank.saveRank();
//
//        Scoreboards.RefreshAll();
//        result.complete(rank);
//
//        return result;
//    }

    public static boolean rename(Faction faction, String oldName, String newName) {
        Rank rank = faction.FindRankByName(oldName);
        if (oldName.equalsIgnoreCase(newName))
            return false;
        rank.name = newName;
        SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE faction='?' AND rank='?'", newName, String.valueOf(faction.id), oldName);
        rank.saveRank();
        Scoreboards.RefreshAll();
        return true;
    }

    public static void delete(Faction faction, String name) {
        Rank rank = faction.FindRankByName(name);
        for (HCFPlayer hcfPlayer : faction.members) {
            if (hcfPlayer.rank.name.equalsIgnoreCase(rank.name)) {
                hcfPlayer.setRank(faction.getDefaultRank());
            }
        }

        SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE faction='?' AND rank='?'",
                faction.getDefaultRank().name,
                String.valueOf(faction.id),
                name
        );
        SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE ID = '?'", String.valueOf(rank.id));
        faction.ranks.remove(rank);
        Main.ranks.remove(rank);
        Scoreboards.RefreshAll();
    }

    public enum Permissions {
        MANAGE_ALL,      // Include faction manager
        MANAGE_MONEY,     // Basic role
        MANAGE_INVITE,    // Can or not invite other players
        MANAGE_RANKS,   // Can modify the faction home
        MANAGE_PLAYERS,  // Can withdraw from the faction balance
        MANAGE_KICK    // Can rename the faction
    }

    public static class Rank {
        public final int id;
        public String name;
        public boolean isDefault;
        public boolean isLeader;
        public int priority;
        public HashMap<Permissions, Boolean> class_permissions = new HashMap<>();

        Rank(int id, String name, boolean isDefault, boolean isLeader) {
            this.name = name;
            this.id = id;
            this.isDefault = isDefault;
            this.isLeader = isLeader;
            for (Permissions p : Permissions.values()) {
                class_permissions.put(p, false);
            }
            //loadPermissions();
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public void setLeader() {
            this.isLeader = true;
        }

        public void loadPermissions() {
            try {
                SQL_Async.dbPollAsync(con, "SELECT * FROM ranks WHERE ID='?'", String.valueOf(id)).thenAcceptAsync(permissionmap -> {
                    if ((Boolean) permissionmap.get("ALL_Permission"))
                        class_permissions.put(Permissions.MANAGE_ALL, true);

                    if ((Boolean) permissionmap.get("MONEY_Permission"))
                        class_permissions.put(Permissions.MANAGE_MONEY, true);

                    if ((Boolean) permissionmap.get("INVITE_Permission"))
                        class_permissions.put(Permissions.MANAGE_INVITE, true);

                    if ((Boolean) permissionmap.get("RANK_Permission"))
                        class_permissions.put(Permissions.MANAGE_RANKS, true);

                    if ((Boolean) permissionmap.get("PLAYER_Permission"))
                        class_permissions.put(Permissions.MANAGE_PLAYERS, true);

                    if ((Boolean) permissionmap.get("KICK_Permission"))
                        class_permissions.put(Permissions.MANAGE_KICK, true);
                    if ((Boolean) permissionmap.get("isDefault")) {
                        isDefault = (Boolean) permissionmap.get("isDefault");
                        priority = -9999;
                    }
                    if ((Boolean) permissionmap.get("isLeader")) {
                        isLeader = (Boolean) permissionmap.get("isLeader");
                        priority = 9999;
                    }
                    priority = (int) permissionmap.get("priority");
                });
            } catch (Exception uwu) {
                uwu.printStackTrace();
            }
        }

        public boolean hasPermission(Permissions perm) {
            if (class_permissions.get(Permissions.MANAGE_ALL))
                return true;
            return class_permissions.get(perm);
        }

        public void setPermission(Permissions perm, boolean state) {
            class_permissions.put(perm, state);
            saveRank();
        }

        public void saveRank() {
            SQL_Connection.dbExecute(con, "UPDATE ranks SET " +
                            "name = '?'," +
                            "ALL_Permission='?'," +
                            "MONEY_Permission='?'," +
                            "INVITE_Permission='?'," +
                            "RANK_Permission='?'," +
                            "PLAYER_Permission='?'," +
                            "KICK_Permission='?'," +
                            "isDefault='?'," +
                            "isLeader='?'," +
                            "priority='?'" +
                            "WHERE ID = '?'",
                    String.valueOf(this.name),
                    (this.class_permissions.get(Permissions.MANAGE_ALL)) ? "1" : "0",
                    (this.class_permissions.get(Permissions.MANAGE_MONEY)) ? "1" : "0",
                    (this.class_permissions.get(Permissions.MANAGE_INVITE)) ? "1" : "0",
                    (this.class_permissions.get(Permissions.MANAGE_RANKS)) ? "1" : "0",
                    (this.class_permissions.get(Permissions.MANAGE_PLAYERS)) ? "1" : "0",
                    (this.class_permissions.get(Permissions.MANAGE_KICK)) ? "1" : "0",
                    (this.isDefault) ? "1" : "0",
                    (this.isLeader) ? "1" : "0",
                    String.valueOf(this.priority),
                    String.valueOf(this.id)
            );
        }
    }
}

