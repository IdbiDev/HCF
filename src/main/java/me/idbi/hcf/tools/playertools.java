package me.idbi.hcf.tools;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.HCF_Rules;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.koth.KOTH;
import org.apache.commons.collections4.map.LinkedMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class playertools {
    private static final Connection con = Main.getConnection("Playertools");

    public static void LoadPlayer(Player player) {
        // Loading player From SQL
        HashMap<String, Object> playerMap = SQL_Connection.dbPoll(con, "SELECT * FROM members WHERE uuid = '?'", player.getUniqueId().toString());
        if (playerMap.size() > 0) {
            Main.player_cache.put(player, new Main.Player_Obj());
            // Találat
            setMetadata(player, "factionid", playerMap.get("faction"));
            setMetadata(player, "kills", playerMap.get("kills"));
            setMetadata(player, "deaths", playerMap.get("deaths"));
            setMetadata(player, "money", playerMap.get("money"));
            setMetadata(player, "class", "none");
            setMetadata(player, "faction", playerMap.get("factionname"));
            setMetadata(player, "adminDuty", false);
            setMetadata(player, "rank", "none");
            setMetadata(player, "freeze", false);
            setMetadata(player, "factionchat", false);
            String c = HCF_Claiming.sendFactionTerritory(player);
            setMetadata(player, "current_loc", c);
            setMetadata(player, "spawnclaiming", false);
            setMetadata(player, "bardenergy", 0D);
            // rankManager.addRankToPlayer(player);
            SQL_Connection.dbExecute(con, "UPDATE members SET online='?' WHERE uuid='?'", "1", player.getUniqueId().toString());
            if (!playerMap.get("faction").equals(0)) {
                Main.faction_cache.get(Integer.valueOf(String.valueOf(playerMap.get("faction")))).ApplyPlayerRank(player, String.valueOf(playerMap.get("rank")));
            }
            Scoreboards.refresh(player);
        } else {
            // Játékos létrehozása SQL-ben, majd újra betöltjük
            SQL_Connection.dbExecute(con, "INSERT INTO members SET name='?',uuid='?',online=0", player.getName(), player.getUniqueId().toString());
            LoadPlayer(player);
        }
    }

    public static List<Player> getPlayersInDistance(Player p, double distance) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getLocation().distance(player.getLocation()) <= distance) {
                players.add(player);
            }
        }
        return players;
    }
    public static Main.Faction getPlayerFaction(Player p) {
        int id = Integer.parseInt(getMetadata(p,"factionid"));
        System.out.println(id);
        if (id != 0){
            return Main.faction_cache.get(id);
        }
        return null;
    }
    public static List<Player> getFactionMembersInDistance(Player p, double distance) {
        String faction = getMetadata(p, "faction");
        List<Player> players = new ArrayList<>();
        for (Player player : getFactionOnlineMembers(faction)) {
            if (p.getLocation().distance(player.getLocation()) <= distance) {
                players.add(player);
            }
        }
        return players;
    }

    public static boolean isFactionOnline(String name) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (getMetadata(p, "faction").equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Player[] getFactionOnlineMembers(String name) {
        List<Player> players = new ArrayList<Player>();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (getMetadata(p, "faction").equals(name)) {
                players.add(p);
            }
        }
        return players.toArray(new Player[0]);
    }

    public static HashMap<String, String> getFactionMembers(int id) {
        HashMap<String,String> players = new HashMap<>();
        try {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM members WHERE faction = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                players.put(rs.getString("name"),rs.getString("uuid"));
            }
            return players;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getPlayerBalance(Player p) {
        return Integer.parseInt(getMetadata(p, "money"));
    }

    public static void setPlayerBalance(Player p, int amount) {
        setMetadata(p, "money", amount);
    }

    public static void setMetadata(Player p, String key, Object data) {
        if (Main.player_cache.containsKey(p)) {
            Main.Player_Obj obj = Main.player_cache.get(p);
            obj.setData(key, data);
        } else {
            if (Main.debug)
                Bukkit.getLogger().severe("SET Nem tartalamzza a playert a cache! >> " + p.getDisplayName());
        }
    }

    public static String getMetadata(Player p, String key) {
        if (Main.player_cache.containsKey(p)) {
            Main.Player_Obj obj = Main.player_cache.get(p);
            return obj.getData(key);
        } else {
            if (Main.debug)
                Bukkit.getLogger().severe("GET Nem tartalamzza a playert a cache! >> " + p.getDisplayName() + "KEY >> " + key);
            return "0";
        }
    }
    public static Object getRealMetadata(Player p, String key) {
        if (Main.player_cache.containsKey(p)) {
            Main.Player_Obj obj = Main.player_cache.get(p);
            return obj.getData(key);
        } else {
            if (Main.debug)
                Bukkit.getLogger().severe("GET Nem tartalamzza a playert a cache! >> " + p.getDisplayName() + "KEY >> " + key);
            return "0";
        }
    }

    public static void setEntityData(Metadatable entity, String key, Object value) {
        entity.setMetadata(key, new FixedMetadataValue(Main.getPlugin(Main.class), value));
    }

    public static String getEntityData(Metadatable entity, String key) {
        return entity.getMetadata(key).get(0).asString();
    }


    public static boolean HasMetaData(Player p, String key) {
        if (Main.player_cache.containsKey(p)) {
            Main.Player_Obj obj = Main.player_cache.get(p);
            return obj.hasData(key);
        } else {
            if (Main.debug)
                Bukkit.getLogger().severe("§4 Nem tartalamzza a playert a cache! >> " + p.getDisplayName());
            return false;
        }
    }

    public static void cacheFactionClaims() {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM claims");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(rs.getInt(3), rs.getInt(5), rs.getInt(4), rs.getInt(6), rs.getInt(2),rs.getString("type"));
                    Main.faction_cache.get(rs.getInt("factionid")).addClaim(claim);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(Player player, rankManager.Permissions perm) {
        return true;
        /*int faction = Integer.parseInt(getMetadata(player,"factionid"));
        if(faction != 0){
            rankManager.Faction_Rank rank = Main.faction_cache.get(faction).player_ranks.get(player);
            if(rank != null){
                return rank.hasPermission(perm);
            }
        }
        return false;*/
    }

    @Deprecated
    public static double getDTR(int faction) {
        return Main.faction_cache.get(faction).DTR;
    }


    public static void setFactionCache() {
        try {
            Main.factionToname.clear();
            Main.faction_cache.clear();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM factions");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Main.factionToname.put(rs.getInt("ID"), rs.getString("name"));
                Main.Faction faction = new Main.Faction(rs.getInt("ID"), rs.getString("name"), rs.getString("leader"), rs.getInt("money"));
                Main.faction_cache.put(rs.getInt("ID"), faction);
                Main.nameToFaction.put(rs.getString("name"), faction);
                faction.memberCount = playertools.countMembers(faction);
                faction.DTR = Double.parseDouble(playertools.CalculateDTR(faction));
                if (Main.debug)
                    Main.sendCmdMessage(faction.factioname + " Prepared");
                if (rs.getString("home") == null)
                    continue;
                Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(rs.getString("home")));
                Location loc = new Location(
                        Bukkit.getWorld(ConfigLibrary.World_name.getValue()),
                        Integer.parseInt(map.get("X").toString()),
                        Integer.parseInt(map.get("Y").toString()),
                        Integer.parseInt(map.get("Z").toString()),
                        Integer.parseInt(map.get("YAW").toString()),
                        Integer.parseInt(map.get("PITCH").toString()));
                faction.setHomeLocation(loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static HashMap<String, String> getPlayersKills() {
        HashMap<String, String> returnHashMap = new HashMap<>();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM members");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String kills = rs.getString("kills");

                returnHashMap.put(rs.getString("name"), kills);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnHashMap;
    }

    public static HashMap<String, List<String>> getRankPlayers(String factionName) {
        HashMap<String, List<String>> returnHashMap = new HashMap<>();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM members");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("factionname").equalsIgnoreCase(factionName)) {
                    String rank = rs.getString("rank");
                    List<String> players;
                    try {
                        players = returnHashMap.get(rank);
                        players.add(rs.getString("name"));
                    } catch (NullPointerException ex) {
                        players = new ArrayList();
                        players.add(rs.getString("name"));
                    }

                    returnHashMap.put(rank, players);

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnHashMap;
    }

    public static void LoadRanks() {
        /*try {
            PreparedStatement rank_ps =  con.prepareStatement("SELECT * FROM ranks");
            ResultSet rank_rs = rank_ps.executeQuery();
            PreparedStatement member_ps =  con.prepareStatement("SELECT * FROM members");
            ResultSet member_rs = member_ps.executeQuery();
            while (rs.next()){
               // rankManager.addRankToCache(rs.getInt("ID"),rs.getInt("faction"), rs.getString("name"), rs.getString("permissions").split(","));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try {
            PreparedStatement rank_ps = con.prepareStatement("SELECT * FROM ranks");
            ResultSet rank_rs = rank_ps.executeQuery();
            while (rank_rs.next()) {
                for (Map.Entry<Integer, Main.Faction> f : Main.faction_cache.entrySet()) {
                    Integer id = f.getKey();
                    Main.Faction faction = f.getValue();
                    if (id.equals(rank_rs.getInt("faction"))) {
                        rankManager.Faction_Rank rank = new rankManager.Faction_Rank(rank_rs.getInt("ID"), rank_rs.getString("name"));
                        rank.loadPermissions();
                        faction.ranks.add(rank);
                        rank.isLeader = rank_rs.getInt("isLeader") == 1;
                        rank.isDefault = rank_rs.getInt("isDefault") == 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }/*
        Member              MAX DTR
        1                       1.5
        2                       2.0
        3                       3.5
        4                       4.0
        5                       5.5

    */

    public static String CalculateDTR(Main.Faction faction){
        return String.valueOf(HCF_Rules.DTR_MEMBERS.get(faction.memberCount));
    }
    public static int countMembers(Main.Faction faction) {
        int count = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM members WHERE faction=?");
            ps.setInt(1,faction.factionid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public static Location getSpawn() {
            String[] spawnLocs = ConfigLibrary.Spawn_location.getValue().split(" ");
        Integer[] ints = getInts(spawnLocs);
        return new Location(Bukkit.getWorld(ConfigLibrary.World_name.getValue()), ints[0], ints[1], ints[2], ints[3], ints[4]);
    }

    public static Integer[] getInts(String[] string) {
        ArrayList<Integer> list = new ArrayList<>();
        for(String str : string) {
            list.add(Integer.parseInt(str));
        }

        return list.toArray(new Integer[list.size()]);
    }

    public static int createCustomFaction(String name,String leader){
        //getting the last minus value
        int id = SQL_Connection.dbExecute(con, "INSERT INTO factions SET name='?',leader='?'", name,leader);
        Main.Faction faction = new Main.Faction(id, name, "", 0);

        Main.faction_cache.put(id, faction);
        Main.factionToname.put(id, faction.factioname);
        Main.nameToFaction.put(faction.factioname, faction);
        return id;
    }


    //Koba, ne nyírj ki ha nem működik gec XD
    //Todo: Check + Bugfix ha van
    public static boolean CheckClaimPlusOne(HCF_Claiming.Point left_c,HCF_Claiming.Point right_c,int diff,HCF_Claiming.Point p1,HCF_Claiming.Point p2){
        //Getting the bottom left point
        int minX = Math.min(left_c.x,right_c.x);
        int minZ = Math.min(left_c.z, right_c.z);

        //Getting the top right point
        int maxX = Math.max(left_c.x,right_c.x);
        int maxZ = Math.max(left_c.z, right_c.z);

        //Creating the new claim
        HCF_Claiming.Point new_claim_start = new HCF_Claiming.Point(minX-diff,minZ-diff);
        HCF_Claiming.Point new_claim_end = new HCF_Claiming.Point(maxX+diff,maxZ+diff);

        return HCF_Claiming.doOverlap2(new_claim_start,new_claim_end,p1,p2);
    }

    public static int getDistanceBetweenPoints2D(HCF_Claiming.Point p1, HCF_Claiming.Point p2) {
        return (Math.abs(p1.x-p2.x) + Math.abs(p1.z-p2.z))-1;
    }
    public static void placeBlockChange(Player p,Location loc){
        if(Main.player_block_changes.containsKey(p)) {
            List<Location> l = Main.player_block_changes.get(p);
            l.add(loc);
            Main.player_block_changes.put(p,l);
        } else {
            List<Location> l = new ArrayList<>();
            l.add(loc);
            Main.player_block_changes.put(p,l);
        }
    }

    public static void prepareKoths() {
        Main.koth_cache.clear();
        HashMap<Integer, Main.Faction> hashMap = Main.faction_cache;

       // LinkedMap<String, Main.Faction> geciFaszAdriánBuziViharvertKurvaRiheÖrömlányLikeAdbi = new LinkedMap<>();

        for (Main.Faction faction : hashMap.values()) {
            for (HCF_Claiming.Faction_Claim claim : faction.claims) {
                if (claim.attribute.equalsIgnoreCase("koth")) {
                    KOTH.koth_area temp = new KOTH.koth_area(
                            faction,
                            new HCF_Claiming.Point(claim.startX, claim.startZ),
                            new HCF_Claiming.Point(claim.endX, claim.endZ)
                    );
                    Main.koth_cache.put(faction.factioname, temp);
                    //eci.put(faction.factioname, temp);
                }
            }
        }
    }
}
