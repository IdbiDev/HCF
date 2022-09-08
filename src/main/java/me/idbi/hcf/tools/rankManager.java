package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class rankManager {
    private static final Connection con = Main.getConnection("rankManager");

    public static Faction_Rank CreateNewRank(Main.Faction faction, String name) {
        for (Faction_Rank rank : faction.ranks) {
            if (rank.name.equals(name)) {
                return null;
            }
        }
        Integer id = SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?'", name, String.valueOf(faction.factionid));
        Faction_Rank rank = new Faction_Rank(id, name);
        rank.loadPermissions();
        faction.ranks.add(rank);
        return rank;
    }

    public static void setDefaultRank(Main.Faction faction, String name) {
        Faction_Rank rank = null;
        for (Faction_Rank r : faction.ranks)
            if (r.name.equals(name)) {
                rank = r;
                break;
            }
        if (rank != null) {
            if (true) {
                rank.isDefault = true;
                SQL_Connection.dbExecute(con, "UPDATE ranks SET isDefault='1' WHERE ID='?'", String.valueOf(rank.id));
            }
        }
    }

    public static void setLeaderRank(Main.Faction faction, String name) {
        Faction_Rank rank = faction.FindRankByName(name);
        if (rank != null) {
            if (true) {
                for (Faction_Rank r : faction.ranks) {
                    if (r.isLeader) {
                        r.isLeader = false;
                        SQL_Connection.dbExecute(con, "UPDATE ranks SET isLeader=0 WHERE ID='?'", String.valueOf(r.id));
                    }
                }
                rank.isLeader = true;
                SQL_Connection.dbExecute(con, "UPDATE ranks SET isLeader=1 WHERE ID='?'", String.valueOf(rank.id));
            }
        }
    }

    public enum Permissions {
        ALL("-1"),      // Include faction manager
        BASIC("0"),     // Basic role
        INVITE("1"),    // Can or not invite other players
        SETHOME("2"),   // Can modify the faction home
        WITHDRAW("3"),  // Can withdraw from the faction balance
        KICK("4");    // Can rename the faction

        private final String value;

        Permissions(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public static class Faction_Rank {
        public String name;
        public Integer id;

        public boolean isDefault = false;
        public boolean isLeader = true;
        public HashMap<String, Boolean> class_permissions = new HashMap<>();

        public Faction_Rank(Integer id, String name) {
            this.name = name;
            this.id = id;
        }

        public void loadPermissions() {
            try {
                HashMap<String, Object> permissionmap = SQL_Connection.dbPoll(con, "SELECT * FROM ranks WHERE ID='?'", String.valueOf(id));
                JSONObject jsonObject = new JSONObject(permissionmap.get("permissions"));
                for (Map.Entry<String, Object> p : JsonUtils.jsonToMap(jsonObject).entrySet()) {
                    String rank = p.getKey();
                    boolean isValid = Boolean.parseBoolean(String.valueOf(p.getValue()));
                    class_permissions.put(rank, isValid);
                }
            } catch (Exception uwu) {
                uwu.printStackTrace();
            }

        }

        public void addPermission(Permissions perms) {
            if (!class_permissions.containsKey(perms.value)) {
                class_permissions.put(perms.value, true);
            }
            System.out.println(class_permissions);
            System.out.println(perms.value);
            UpdatePermissions();
        }

        public void removePermission(Permissions perms) {
            class_permissions.remove(perms.value);
            UpdatePermissions();
        }

        public boolean hasPermission(Permissions perm) {
            if (class_permissions.containsKey(Permissions.ALL.value)) {
                return true;
            }
            return class_permissions.containsKey(perm.value);
        }

        public void UpdatePermissions() {
            JSONObject obj = new JSONObject(class_permissions);
            SQL_Connection.dbExecute(con, "UPDATE ranks SET permissions = '?' WHERE ID = '?'", obj.toString(), String.valueOf(id));

        }
    }
}
