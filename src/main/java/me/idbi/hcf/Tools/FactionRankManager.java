package me.idbi.hcf.Tools;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Database.MongoDB.AsyncMongoDBDriver;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MongoDB.MongoFields;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Async;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;


public class FactionRankManager {
    public static Connection con = Playertools.con;

    public static Rank create(Player player, String name, boolean isLeader, boolean isDefault) {
        Faction faction = Playertools.getPlayerFaction(player);
        assert faction != null;
        for (FactionRankManager.Rank rank : faction.getRanks())
            if (rank.getName().equalsIgnoreCase(name))
                return null;

        int id = Playertools.getFreeRankId();
        int priority = 0;
        if (isLeader)
            priority = 9999;
        else if (isDefault)
            priority = -9999;
        else
            priority = -9999 + faction.getRanks().size();
        if(Main.isUsingMongoDB()){
            Document insert = new Document();
            insert.append(MongoFields.RanksFields.NAME.get(), name);
            insert.append(MongoFields.RanksFields.FACTION.get(),faction.getId());
            insert.append(MongoFields.RanksFields.ISDEFAULT.get(),isDefault);
            insert.append(MongoFields.RanksFields.ISLEADER.get(),isLeader);
            insert.append(MongoFields.RanksFields.PRIORITY.get(),priority);
            insert.append(MongoFields.RanksFields.ID.get(),id);

            HashMap<String, Boolean> _temp = new HashMap<>();
            _temp.put(Permissions.MANAGE_ALL.name(),false);
            _temp.put(Permissions.MANAGE_MONEY.name(),true);
            _temp.put(Permissions.MANAGE_RANKS.name(),false);
            _temp.put(Permissions.MANAGE_INVITE.name(),false);
            _temp.put(Permissions.MANAGE_KICK.name(),false);
            _temp.put(Permissions.MANAGE_PLAYERS.name(),false);

            insert.append(MongoFields.RanksFields.PERMISSIONS.get(), new JSONObject(_temp).toString());

            MongoDBDriver.Insert(MongoDBDriver.MongoCollections.RANKS, insert);
    }else {
            SQL_Connection.dbExecute(con, "INSERT INTO ranks SET name = '?', faction='?', isDefault='?',isLeader='?',priority='?', ID='?'",
                    name, String.valueOf(faction.getId()),
                    (isDefault) ? "1" : "0",
                    (isLeader) ? "1" : "0",
                    priority + "",
                    String.valueOf(id));
        }
        Rank rank = new Rank(id, name, isDefault, isLeader);
        rank.setPriority(priority);
        Main.ranks.add(rank);
        faction.addRank(rank);
        Scoreboards.refreshAll();
        //rank.saveRank();
        faction.sortRanks();
        return rank;
    }

    public static boolean rename(Faction faction, String oldName, String newName) {
        con = Main.getCon();
        Rank rank = faction.getRankByName(oldName);
        if (oldName.equalsIgnoreCase(newName))
            return false;
        rank.setName(newName);
        if(Main.isUsingMongoDB()){
            Bson update = set("rank",newName);
            Bson where = and(eq("rank",oldName), eq("faction",faction.getId()));
            MongoDBDriver.UpdateMany(MongoDBDriver.MongoCollections.MEMBERS,where,update);
        }else {
            SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE faction='?' AND rank='?'", newName, String.valueOf(faction.getId()), oldName);
        }
        rank.saveRank();
        Scoreboards.refreshAll();
        faction.sortRanks();
        return true;
    }

    public static void delete(Faction faction, String name) {
        Rank rank = faction.getRankByName(name);
        for (Player player : faction.getOnlineMembers()) {
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
            if (hcfPlayer.getRank().getName().equalsIgnoreCase(rank.getName())) {
                hcfPlayer.setRank(faction.getDefaultRank());
            }
        }
        if(Main.isUsingMongoDB()) {
            Bson where = and(eq(MongoFields.MembersFields.RANK.get(), name), eq(MongoFields.MembersFields.FACTION.get(), faction.getId()));
            Bson update = set(MongoFields.MembersFields.RANK.get(), faction.getDefaultRank().getName());
            MongoDBDriver.Update(MongoDBDriver.MongoCollections.MEMBERS, where, update);

            MongoDBDriver.Delete(MongoDBDriver.MongoCollections.RANKS,eq("ID",rank.id));
        } else {
            SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE faction='?' AND rank='?'",
                    faction.getDefaultRank().getName(),
                    String.valueOf(faction.getId()),
                    name
            );
            SQL_Connection.dbExecute(con, "DELETE FROM ranks WHERE ID = '?'", String.valueOf(rank.id));
        }
        faction.removeRank(rank);
        Main.ranks.remove(rank);
        Scoreboards.refreshAll();
        faction.sortRanks();
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
        @Getter private final int id;
        @Getter @Setter private String name;
        @Getter @Setter private boolean isDefault;
        @Getter @Setter private boolean isLeader;
        @Getter @Setter private int priority;
        private final HashMap<Permissions, Boolean> class_permissions = new HashMap<>();

        Rank(int id, String name, boolean isDefault, boolean isLeader) {
            this.name = name;
            this.id = id;
            this.isDefault = isDefault;
            this.isLeader = isLeader;
            for (Permissions p : Permissions.values()) {
                class_permissions.put(p, false);
            }
            loadPermissions();
        }

        public HashMap<Permissions, Boolean> getClassPermissions() {
            return this.class_permissions;
        }

        public void loadPermissions() {
            try {
                if(Main.isUsingMongoDB()){
                    AsyncMongoDBDriver.Find(MongoDBDriver.MongoCollections.RANKS,eq(MongoFields.RanksFields.ID.get(),id)).thenAcceptAsync(res ->{
                        JSONObject ret = new JSONObject(res.getString(MongoFields.RanksFields.PERMISSIONS.get()));
                        for(Permissions p : Permissions.values()) {
                            class_permissions.put(p,ret.getBoolean(p.name()));
                        }
                        priority = res.getInteger(MongoFields.RanksFields.PRIORITY.get());
                        isDefault = res.getBoolean(MongoFields.RanksFields.ISDEFAULT.get());
                        isLeader = res.getBoolean(MongoFields.RanksFields.ISLEADER.get());
                    });
                }else {
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
                }
            } catch (Exception uwu) {
                uwu.printStackTrace();
            }
        }

        public boolean hasPermission(Permissions perm) {
            if (class_permissions.get(Permissions.MANAGE_ALL))
                return true;
            if (this.isLeader)
                return true;
            return class_permissions.get(perm);
        }

        public void setPermission(Permissions perm, boolean state) {
            class_permissions.put(perm, state);
            saveRank();
        }

        public void saveRank() {
            if(Main.isUsingMongoDB()){
                Bson where = eq(MongoFields.RanksFields.ID.get(),id);
                HashMap<String, Boolean> _temp = new HashMap<>();
                for(Map.Entry<Permissions, Boolean> p : this.class_permissions.entrySet()){
                    _temp.put(p.getKey().name(), p.getValue());
                }
                JSONObject jsonObject = new JSONObject(_temp);
                Bson updates = combine( set(MongoFields.RanksFields.NAME.get(),this.name),
                        set(MongoFields.RanksFields.PERMISSIONS.get(),jsonObject.toString()),
                        set(MongoFields.RanksFields.ISDEFAULT.get(),this.isDefault),
                        set(MongoFields.RanksFields.ISLEADER.get(),this.isLeader),
                        set(MongoFields.RanksFields.PRIORITY.get(), this.priority));
                MongoDBDriver.Update(MongoDBDriver.MongoCollections.RANKS,where,updates);
            }else {
                //System.out.println("Priority for: " + this.name + " IS: " + priority);
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
        public void saveRankSync() {
            if(Main.isUsingMongoDB()) {
                Bson where = eq(MongoFields.RanksFields.ID.get(),id);
                HashMap<String, Boolean> _temp = new HashMap<>();
                for(Map.Entry<Permissions, Boolean> p : this.class_permissions.entrySet()){
                    _temp.put(p.getKey().name(), p.getValue());
                }
                JSONObject jsonObject = new JSONObject(_temp);
                Bson updates = combine( set(MongoFields.RanksFields.NAME.get(),this.name),
                        set(MongoFields.RanksFields.PERMISSIONS.get(),jsonObject.toString()),
                        set(MongoFields.RanksFields.ISDEFAULT.get(),this.isDefault),
                        set(MongoFields.RanksFields.ISLEADER.get(),this.isLeader),
                        set(MongoFields.RanksFields.PRIORITY.get(), this.priority));
                MongoDBDriver.Update(MongoDBDriver.MongoCollections.RANKS,where,updates);
            }else{
                    //System.out.println("Priority for: " + this.name + " IS: " + priority);
                    SQL_Connection.dbSyncExec(con, "UPDATE ranks SET " +
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
}

