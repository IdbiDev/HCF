package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;

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

    public static void LoadPlayer(Player player){
        // Loading player From SQL
        HashMap<String, Object> playerMap = SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid = '?'",player.getUniqueId().toString());
        if (playerMap.size() > 0){
            Main.player_cache.put(player,new Main.Player_Obj());
            // Találat
            setMetadata(player,"factionid",playerMap.get("faction"));
            setMetadata(player,"kills",playerMap.get("kills"));
            setMetadata(player,"deaths",playerMap.get("deaths"));
            setMetadata(player,"money",playerMap.get("money"));
            setMetadata(player,"class","none");
            setMetadata(player,"faction",playerMap.get("factionname"));
            setMetadata(player,"adminDuty",true);
            setMetadata(player,"rank","none");
            setMetadata(player,"freeze",false);
            String c = HCF_Claiming.sendFactionTerretory(player);
            setMetadata(player,"current_loc",c);
           // rankManager.addRankToPlayer(player);
            SQL_Connection.dbExecute(con,"UPDATE members SET online='?' WHERE uuid='?'","1",player.getUniqueId().toString());
            if(!playerMap.get("faction").equals(0)){
                Main.faction_cache.get(Integer.valueOf(String.valueOf(playerMap.get("faction")))).ApplyPlayerRank(player, String.valueOf(playerMap.get("rank")));
            }
            Scoreboards.refresh(player);

        }else{
            // Játékos létrehozása SQL-ben, majd újra betöltjük
            SQL_Connection.dbExecute(con,"INSERT INTO members SET name='?',uuid='?',online=0",player.getName(),player.getUniqueId().toString());
            LoadPlayer(player);
        }
    }

    public static List<Player> getPlayersInDistance(Player p, double distance){
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            if (p.getLocation().distance(player.getLocation()) <= distance){
                players.add(player);
            }
        }
        return players;
    }
    public static List<Player> getFactionMembersInDistance(Player p, double distance){
        String faction = getMetadata(p,"faction");
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()){
            if (p.getLocation().distance(player.getLocation()) <= distance && getMetadata(player,"faction").equals(faction)){
                players.add(player);
            }
        }
        return players;
    }

    public static boolean isFactionOnline(String name){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if (getMetadata(p,"faction").equals(name)){
                return true;
            }
        }
        return false;
    }
    public static Player[] getFactionOnlineMembers(String name){
        List<Player> players = new ArrayList<Player>();
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if (getMetadata(p,"faction").equals(name)){
                players.add(p);
            }
        }
        return players.toArray(new Player[0]);
    }
    public static  HashMap<String, HashMap<String, String>> getFactionMembers(String name)  {
        HashMap<String, Object> faction = SQL_Connection.dbPoll(con,"SELECT * FROM factions WHERE name = '?'",name);
        HashMap<String, HashMap<String, String>> players = new HashMap<>();
        try {

            PreparedStatement ps =  con.prepareStatement("SELECT * FROM members WHERE faction = ?");
            ps.setString(1, faction.get("ID").toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                HashMap<String, String> stats = new HashMap(){{
                    put("Rank",rs.getString(4));
                    put("Kills",rs.getString(5));
                    put("Deaths",rs.getString(6));
                    put("Money",rs.getString(7));
                    put("Online",rs.getString(9));

                }};
                players.put(rs.getString(2),stats);
            }
            return players;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int getPlayerBalance(Player p){
        return Integer.parseInt(getMetadata(p,"money"));
    }
    public static void setPlayerBalance(Player p,int amount){
        setMetadata(p,"money",amount);
    }
    public static void setMetadata(Player p, String key, Object data){
        if(Main.player_cache.containsKey(p)){
            Main.Player_Obj obj = Main.player_cache.get(p);
            obj.setData(key,data);
        }else{
            if(Main.debug)
                Bukkit.getLogger().severe("§4 Nem tartalamzza a playert a cache! >> " + p.getDisplayName());
        }
    }
    public static String getMetadata(Player p, String key){
        if(Main.player_cache.containsKey(p)){
            Main.Player_Obj obj = Main.player_cache.get(p);
            return obj.getData(key);
        }else{
            if(Main.debug)
                Bukkit.getLogger().severe("§4 Nem tartalamzza a playert a cache! >> " + p.getDisplayName());
            return "0";
        }
    }
    public static void setEntityData(Metadatable entity, String key, Object value){
        entity.setMetadata(key,new FixedMetadataValue(Main.getPlugin(Main.class),value));
    }

    public static String getEntityData(Metadatable entity, String key){
        return entity.getMetadata(key).get(0).asString();
    }
    public static boolean HasMetaData(Player p,String key){
        if(Main.player_cache.containsKey(p)){
            Main.Player_Obj obj = Main.player_cache.get(p);
            return obj.hasData(key);
        }else{
            if(Main.debug)
                Bukkit.getLogger().severe("§4 Nem tartalamzza a playert a cache! >> " + p.getDisplayName());
            return false;
        }
    }
    public static void cacheFactionClaims(){
        try {
            PreparedStatement ps =  con.prepareStatement("SELECT * FROM claims");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                if(rs.getString("type").equals("faction")){
                    try{
                        HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(rs.getInt(3),rs.getInt(5),rs.getInt(4),rs.getInt(6),rs.getInt(2));
                        Main.faction_cache.get(rs.getInt("factionid")).addClaim(claim);
                    }catch (Exception ignored){

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(Player player,rankManager.Permissions perm){
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
    public static int getDTR(int faction){
        HashMap<String, Object> factionmap = SQL_Connection.dbPoll(con,"SELECT * FROM factions WHERE ID = '?'", String.valueOf(faction));
        if(!factionmap.isEmpty()){
            return (int) factionmap.get("DTR");
        }
        return 0;
    }


    public static void setFactionCache(){
        try {
            Main.factionToname.clear();
            Main.faction_cache.clear();
            PreparedStatement ps =  con.prepareStatement("SELECT * FROM factions");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Main.factionToname.put(rs.getInt("ID"), rs.getString("name"));
                Main.Faction faction = new Main.Faction(rs.getInt("ID"), rs.getString("name"), rs.getString("leader"), rs.getInt("money"));
                Main.faction_cache.put(rs.getInt("ID"), faction);
                Main.nameToFaction.put(rs.getString("name"), faction);
                if(rs.getString("home") == null)
                    continue;
                Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(rs.getString("home")));
                Location loc = new Location(
                        Bukkit.getWorld(ConfigLibrary.World_name.getValue()),
                        Integer.parseInt(map.get("X").toString()),
                        Integer.parseInt(map.get("Y").toString()),
                        Integer.parseInt(map.get("Z").toString()),
                        Integer.parseInt(map.get("YAW").toString()),
                        Integer.parseInt(map.get("PITCH").toString()));
                System.out.println(loc);
                faction.setHomeLocation(loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void BroadcastFaction(String name,String message){
        Player[] members = getFactionOnlineMembers(name);
        for (Player member : members) {
            member.sendMessage(message);
        }

    }

    public static HashMap<String, String> getPlayersKills() {
        HashMap<String, String> returnHashMap = new HashMap<>();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM members");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
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

            while(rs.next()) {
                if(rs.getString("factionname").equalsIgnoreCase(factionName)) {
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
        try{
            PreparedStatement rank_ps =  con.prepareStatement("SELECT * FROM ranks");
            ResultSet rank_rs = rank_ps.executeQuery();
            while (rank_rs.next()){
                for(Map.Entry<Integer, Main.Faction> f : Main.faction_cache.entrySet()){
                    Integer id = f.getKey();
                    Main.Faction faction = f.getValue();
                    if(id.equals(rank_rs.getInt("faction"))){
                        rankManager.Faction_Rank rank = new rankManager.Faction_Rank(rank_rs.getInt("ID"), rank_rs.getString("name"));
                        rank.loadPermissions();
                        faction.ranks.add(rank);
                        if(rank_rs.getInt("isLeader") == 1){
                            rank.isLeader = true;
                        }
                        if(rank_rs.getInt("isDefault") == 1){
                            rank.isDefault = true;
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
