package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class Faction_Rank_Manager {
    public enum Permissions {
        ALL,      // Include faction manager
        BASIC,     // Basic role
        INVITE,    // Can or not invite other players
        SETHOME,   // Can modify the faction home
        WITHDRAW,  // Can withdraw from the faction balance
        KICK    // Can rename the faction
    }
    private static final Connection con = Main.getConnection("rankManagerNew");
    public static Rank CreateRank(Main.Faction faction, String name) {
        for (Faction_Rank_Manager.Rank rank : faction.ranks)
            if (rank.name.equals(name))
                return null;


        int id = SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?'",
                name,
                String.valueOf(faction.factionid)
        );
        Rank rank = new Rank(id, name);
        faction.ranks.add(rank);
        rank.saveRank();
        return rank;
    }
    public static Rank CreateRank(Player player, String name) {
        Main.Faction faction = playertools.getPlayerFaction(player);
        assert faction != null;
        for (Faction_Rank_Manager.Rank rank : faction.ranks)
            if (rank.name.equals(name))
                return null;
        int id = SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?'",
                name,
                String.valueOf(faction.factionid)
        );
        Rank rank = new Rank(id, name);
        faction.ranks.add(rank);
        rank.saveRank();
        return rank;
    }
    public static boolean RenameRank(Main.Faction faction,String oldName,String newName) {
        Rank rank = faction.FindRankByName(oldName);
        if(oldName.equalsIgnoreCase(newName))
            return false;
        for(Map.Entry<Player, Rank> entry : faction.player_ranks.entrySet())
            if(entry.getValue().name.equals(rank.name))
                playertools.setMetadata(entry.getKey(), "rank", newName);
        rank.name = newName;
        SQL_Connection.dbExecute(con,"UPDATE members SET rank='?' WHERE faction='?' AND rank='?'",newName, String.valueOf(faction.factionid),oldName);
        rank.saveRank();

        return true;
    }
    public static void DeleteRank(Main.Faction faction, String name) {
        Rank rank = faction.FindRankByName(name);
        for(Map.Entry<Player, Rank> entry : faction.player_ranks.entrySet()) {
            if (entry.getValue().name.equals(rank.name)) {
                playertools.setMetadata(entry.getKey(), "rank", faction.getDefaultRank().name);
                entry.setValue(faction.getDefaultRank());
            }
        }
        SQL_Connection.dbExecute(con,"UPDATE members SET rank='?' WHERE faction='?' AND rank='?'",
                faction.getDefaultRank().name,
                String.valueOf(faction.factionid),
                name
        );
        SQL_Connection.dbExecute(con,"DELETE FROM ranks WHERE ID = '?'", rank.id.toString());
        faction.ranks.remove(rank);
    }


    public static class Rank {
        public String name;
        private final Integer id;
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
                if(permissionmap.get("ALL_Permission").equals("1"))
                    class_permissions.put(Permissions.ALL,true);

                if(permissionmap.get("BASIC_Permission").equals("1"))
                    class_permissions.put(Permissions.BASIC,true);

                if(permissionmap.get("INVITE_Permission").equals("1"))
                    class_permissions.put(Permissions.INVITE,true);

                if(permissionmap.get("SETHOME_Permission").equals("1"))
                    class_permissions.put(Permissions.SETHOME,true);

                if(permissionmap.get("WITHDRAW_Permission").equals("1"))
                    class_permissions.put(Permissions.WITHDRAW,true);

                if(permissionmap.get("KICK_Permission").equals("1"))
                    class_permissions.put(Permissions.ALL,true);

            } catch (Exception uwu) {
                uwu.printStackTrace();
            }
        }
        public boolean hasPermission(Permissions perm){
            if(class_permissions.get(Permissions.ALL))
                return true;
            return class_permissions.get(perm);
        }
        public void setPermission(Permissions perm,boolean state) {
            class_permissions.put(perm,state);
            saveRank();
        }

        private void saveRank(){
            SQL_Connection.dbExecute(con,"UPDATE ranks SET " +
                            "name = '?'," +
                            "ALL_Permission='?'," +
                            "BASIC_Permission='?'," +
                            "INVITE_Permission='?'," +
                            "SETHOME_Permission='?'," +
                            "WITHDRAW_Permission='?'," +
                            "KICK_Permission='?'" +
                            "WHERE ID = '?'",
                    String.valueOf(this.name),
                    (class_permissions.get(Permissions.ALL)) ? "1" : "0",
                    (class_permissions.get(Permissions.BASIC)) ? "1" : "0",
                    (class_permissions.get(Permissions.INVITE)) ? "1" : "0",
                    (class_permissions.get(Permissions.SETHOME)) ? "1" : "0",
                    (class_permissions.get(Permissions.WITHDRAW)) ? "1" : "0",
                    (class_permissions.get(Permissions.KICK)) ? "1" : "0",
                    String.valueOf(this.id)
            );
        }
    }
}

