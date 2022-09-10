package me.idbi.hcf.tools;

import me.idbi.hcf.Main;

import java.sql.Connection;
import java.util.HashMap;


public class Faction_Rank_Manager {
    public enum Permissions {
        ALL,      // Include faction manager
        BASIC,     // Basic role
        INVITE,    // Can or not invite other players
        SETHOME,   // Can modify the faction home
        WITHDRAW,  // Can withdraw from the faction balance
        KICK;    // Can rename the faction
    }
    private static final Connection con = Main.getConnection("rankManagerNew");
    public static Rank CreateRank(Main.Faction faction, String name) {
        for (rankManager.Faction_Rank rank : faction.ranks) {
            if (rank.name.equals(name)) {
                return null;
            }
        }
        int id = SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?'", name, String.valueOf(faction.factionid));
        Rank rank = new Rank(id, name);
        faction.ranks.add(rank);
        return rank;
    }
    public static class Rank {
        public String name;
        public Integer id;

        public boolean isDefault = false;
        public boolean isLeader = false;
        public HashMap<Permissions, Boolean> class_permissions = new HashMap<>();
        Rank(int id,String name){
            this.name = name;
            this.id = id;
            for(Permissions p : Permissions.values()){
                class_permissions.put(p,false);
            }
            loadPermissions();
        }
        private void loadPermissions() {
            try {
                HashMap<String, Object> permissionmap = SQL_Connection.dbPoll(con, "SELECT * FROM ranks WHERE ID='?'", String.valueOf(id));
                if(permissionmap.get("ALL_Permission").equals("1")){
                    class_permissions.put(Permissions.ALL,true);
                }
                if(permissionmap.get("BASIC_Permission").equals("1")){
                    class_permissions.put(Permissions.BASIC,true);
                }
                if(permissionmap.get("INVITE_Permission").equals("1")){
                    class_permissions.put(Permissions.INVITE,true);
                }
                if(permissionmap.get("SETHOME_Permission").equals("1")){
                    class_permissions.put(Permissions.SETHOME,true);
                }
                if(permissionmap.get("WITHDRAW_Permission").equals("1")){
                    class_permissions.put(Permissions.WITHDRAW,true);
                }
                if(permissionmap.get("KICK_Permission").equals("1")){
                    class_permissions.put(Permissions.ALL,true);
                }
            } catch (Exception uwu) {
                uwu.printStackTrace();
            }
        }
        public boolean hasPermission(Permissions perm){
            return class_permissions.get(perm);
        }
        public void setPermission(Permissions perm,boolean state) {
            class_permissions.put(perm,state);
        }

    }
}

