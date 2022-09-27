package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class Faction_Rank_Manager {
    public enum Permissions {
        MANAGE_ALL,      // Include faction manager
        MANAGE_MONEY,     // Basic role
        MANAGE_INVITE,    // Can or not invite other players
        MANAGE_RANKS,   // Can modify the faction home
        MANAGE_PLAYERS,  // Can withdraw from the faction balance
        MANAGE_KICK    // Can rename the faction
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
        Scoreboards.RefreshAll();
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
        Scoreboards.RefreshAll();
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
        Scoreboards.RefreshAll();
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
        Scoreboards.RefreshAll();
    }


    public static class Rank {
        public String name;
        private final Integer id;
        public boolean isDefault = false;
        public boolean isLeader = false;
        public int priority = 0;
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
                if( (Boolean) permissionmap.get("ALL_Permission"))
                    class_permissions.put(Permissions.MANAGE_ALL,true);

                if((Boolean) permissionmap.get("MONEY_Permission"))
                    class_permissions.put(Permissions.MANAGE_MONEY,true);

                if((Boolean) permissionmap.get("INVITE_Permission"))
                    class_permissions.put(Permissions.MANAGE_INVITE,true);

                if((Boolean) permissionmap.get("RANK_Permission"))
                    class_permissions.put(Permissions.MANAGE_RANKS,true);

                if((Boolean) permissionmap.get("PLAYER_Permission"))
                    class_permissions.put(Permissions.MANAGE_PLAYERS,true);

                if((Boolean) permissionmap.get("KICK_Permission"))
                    class_permissions.put(Permissions.MANAGE_KICK,true);

                if((Boolean) permissionmap.get("isDefault"))
                    isDefault = (Boolean) permissionmap.get("isDefault");

                if((Boolean) permissionmap.get("isLeader"))
                    isLeader = (Boolean)  permissionmap.get("isLeader");
            } catch (Exception uwu) {
                uwu.printStackTrace();
            }
        }
        public boolean hasPermission(Permissions perm){
            if(class_permissions.get(Permissions.MANAGE_ALL))
                return true;
            return class_permissions.get(perm);
        }
        public void setPermission(Permissions perm,boolean state) {
            class_permissions.put(perm,state);
            saveRank();
        }

        public void saveRank(){
            SQL_Connection.dbExecute(con,"UPDATE ranks SET " +
                            "name = '?'," +
                            "ALL_Permission='?'," +
                            "MONEY_Permission='?'," +
                            "INVITE_Permission='?'," +
                            "RANK_Permission='?'," +
                            "PLAYER_Permission='?'," +
                            "KICK_Permission='?'," +
                            "isDefault='?'," +
                            "isLeader='?'" +
                            "WHERE ID = '?'",
                    String.valueOf(this.name),
                    (class_permissions.get(Permissions.MANAGE_ALL)) ? "1" : "0",
                    (class_permissions.get(Permissions.MANAGE_MONEY)) ? "1" : "0",
                    (class_permissions.get(Permissions.MANAGE_INVITE)) ? "1" : "0",
                    (class_permissions.get(Permissions.MANAGE_RANKS)) ? "1" : "0",
                    (class_permissions.get(Permissions.MANAGE_PLAYERS)) ? "1" : "0",
                    (class_permissions.get(Permissions.MANAGE_KICK)) ? "1" : "0",
                    (isDefault) ? "1" : "0",
                    (isLeader) ? "1" : "0",
                    String.valueOf(this.id)
            );
        }
    }
}

