package me.idbi.hcf.tools;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.PlayerObject;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static me.idbi.hcf.Main.ranks;


public class playertools {
    private static final Connection con = Main.getConnection("Playertools");


    public static void LoadPlayer(Player player) {

        // Loading player From SQL
        HashMap<String, Object> playerMap = SQL_Connection.dbPoll(con, "SELECT * FROM members WHERE uuid = '?'", player.getUniqueId().toString());
        if (!playerMap.isEmpty()) {
            Main.player_cache.put(player, new PlayerObject());
            setMetadata(player, "factionid", playerMap.get("faction"));
            setMetadata(player, "kills", playerMap.get("kills"));
            setMetadata(player, "deaths", playerMap.get("deaths"));
            setMetadata(player, "money", playerMap.get("money"));
            setMetadata(player, "class", "None");
            setMetadata(player, "faction", playerMap.get("factionname"));
            setMetadata(player, "adminDuty", false);
            setMetadata(player, "rank", "None");
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
            PlayerStatistic statistic = new PlayerStatistic(new JSONObject(playerMap.get("statistics").toString()));
            statistic.kills = (int) playerMap.get("kills");
            statistic.deaths = (int) playerMap.get("deaths");
            Main.playerStatistics.put(player.getUniqueId(),statistic);
            NameChanger.refresh(player);
        } else {
            // Játékos létrehozása SQL-ben, majd újra betöltjük
            SQL_Connection.dbExecute(con, "INSERT INTO members SET name='?',uuid='?',online=0,money='?'", player.getName(), player.getUniqueId().toString(),Config.default_balance.asStr());
            JSONObject jsonComp = new JSONObject();
            JSONArray factions = new JSONArray();
            //JSONArray ClassTimes = new JSONArray();
            JSONObject classTimes = new JSONObject();
            classTimes.put("Bard",0);
            classTimes.put("Assassin",0);
            classTimes.put("Archer",0);
            classTimes.put("Miner",0);
            classTimes.put("Total",0);
            jsonComp.put("totalFactions", 0);
            jsonComp.put("MoneySpend", 0);
            jsonComp.put("MoneyEarned", 0);
            jsonComp.put("TimePlayed", 0);
            jsonComp.put("startDate", new Date().getTime());
            jsonComp.put("lastLogin",new Date().getTime());
            jsonComp.put("FactionHistory", factions);
            jsonComp.put("ClassTimes", classTimes);
            PlayerStatistic m = new PlayerStatistic(jsonComp);
            m.Save(player);
            //SQL_Connection.dbExecute(con, "INSERT INTO playerstatistics SET uuid='?',StartDate='?',LastLogin='?'", player.getUniqueId().toString(), new Date().toInstant().toString(),new Date().toInstant().toString());
            LoadPlayer(player);
        }
    }

    //OnJoin
    public static void loadOnlinePlayer(Player player) {
        if(!player.hasPlayedBefore() || !Main.player_cache.containsKey(player.getUniqueId())) {
            System.out.println("NEW PLAYER UWU");
            SQL_Connection.dbExecute(con, "INSERT INTO members SET name='?',uuid='?',money='?'", player.getName(), player.getUniqueId().toString(),Config.default_balance.asInt() + "");
            JSONObject jsonComp = new JSONObject();
            JSONArray factions = new JSONArray();
            //JSONArray ClassTimes = new JSONArray();
            JSONObject classTimes = new JSONObject();
            classTimes.put("Bard",0);
            classTimes.put("Assassin",0);
            classTimes.put("Archer",0);
            classTimes.put("Miner",0);
            classTimes.put("Total",0);
            jsonComp.put("totalFactions", 0);
            jsonComp.put("MoneySpend", 0);
            jsonComp.put("MoneyEarned", 0);
            jsonComp.put("TimePlayed", 0);
            jsonComp.put("startDate", new Date().getTime());
            jsonComp.put("lastLogin", new Date().getTime());
            jsonComp.put("FactionHistory", factions);
            jsonComp.put("ClassTimes", classTimes);
            PlayerStatistic thisM = new PlayerStatistic(jsonComp);
            HCFPlayer.createPlayer(player);
            thisM.save(player.getUniqueId());
        }

        HCFPlayer hcf = HCFPlayer.getPlayer(player);
        hcf.currentArea = HCF_Claiming.getPlayerArea(player);

        Scoreboards.refresh(player);
        NameChanger.refresh(player);
    }

    public static void cacheAll() {
        boolean shouldUseAsync = Bukkit.getOnlinePlayers().size() == 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM members");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                if(!shouldUseAsync)
                    cachePlayerSync(UUID.fromString(rs.getString("uuid")), rs);
                else
                    cachePlayer(UUID.fromString(rs.getString("uuid")));
                Main.sendCmdMessage("Player " + rs.getString("uuid") + " cached!");
            }
        } catch (SQLException e){
            e.printStackTrace();
            
        }
    }
    public static void cachePlayerSync(UUID uuid, ResultSet rs) {
        try {
            HCFPlayer hcf = new HCFPlayer(
                    uuid,
                    rs.getInt("kills"),
                    rs.getInt("deaths"),
                    Main.faction_cache.get(rs.getInt("faction")),
                    rs.getInt("money"),
                    new PlayerStatistic(new JSONObject(rs.getString("statistics"))),
                    rs.getString("rank"),
                    rs.getString("language")
            );
            System.out.println(uuid.toString() + "Done loading! Some info:");
            System.out.println("Faction: " + hcf.faction);
                System.out.println("Name: " + hcf.name);
                System.out.println("Rank: " + hcf.rank);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Reload / Restart / Mindig létezik
    public static void cachePlayer(UUID uuid) {
        AsyncSQL.dbPollAsync(con, "SELECT * FROM members WHERE uuid='?'",uuid.toString()).thenAcceptAsync(playerMap -> {
            if (!playerMap.isEmpty()) {
                HCFPlayer hcf = new HCFPlayer(
                        uuid,
                        (int) playerMap.get("kills"),
                        (int) playerMap.get("deaths"),
                        Main.faction_cache.get((int) playerMap.get("faction")),
                        (int) playerMap.get("money"),
                        new PlayerStatistic(new JSONObject(playerMap.get("statistics").toString())),
                        playerMap.get("rank").toString(),
                        playerMap.get("language").toString()
                );
                System.out.println(uuid.toString() + "Done loading! Some info:");
                /*System.out.println("Faction: " + hcf.faction);
                System.out.println("Name: " + hcf.name);
                System.out.println("Rank: " + hcf.rank);*/
            }
        });
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
    public static Faction getPlayerFaction(Player p) {
        int id = Integer.parseInt(getMetadata(p,"factionid"));
        if (id != 0){
            return Main.faction_cache.get(id);
        }
        return null;
    }
    public static Faction getPlayerFaction(OfflinePlayer p) {
        HashMap<String, Object> playerMap = SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?'",p.getUniqueId().toString());
        int id = (int) playerMap.get("faction");
        if (id != 0){
            return Main.faction_cache.get(id);
        }
        return null;
    }
    public static List<Player> getFactionMembersInDistance(Player p, double distance) {
        Faction faction = getPlayerFaction(p);
        List<Player> players = new ArrayList<>();
        for (Player player : getFactionOnlineMembers(faction)) {
            if (p.getLocation().distance(player.getLocation()) <= distance) {
                players.add(player);
            }
        }
        return players;
    }

    public static boolean isFactionOnline(Faction faction) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (getMetadata(p, "faction").equalsIgnoreCase(faction.name)) {
                return true;
            }
        }
        return false;
    }
    public static Faction getFactionByName(String name){
        for (Map.Entry<Integer, Faction> entry : Main.faction_cache.entrySet())
        {
            if(entry.getValue().name.equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }
    public static  ArrayList<Player> getFactionOnlineMembers(Faction faction) {
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (faction.isPlayerInFaction(p)){
                players.add(p);
            }
        }
        return players;
    }

    public static ArrayList<String> getFactionMembers(int id) {
        ArrayList<String> players = new ArrayList<>();
        Faction f = Main.faction_cache.get(id);
        assert null != f;
        for (HCFPlayer member : f.members) {
            players.add(member.name);
        }
        return players;
    }
    public static void RenameFaction(Faction faction,String name){
        for(Player p : getFactionOnlineMembers(faction)){
            setMetadata(p,"faction",name);
        }
        String oldname = faction.name;
        faction.name = name;
        Main.faction_cache.put(faction.id,faction);
        Main.nameToFaction.put(faction.name,faction);
        Main.factionToname.put(faction.id, faction.name);
        SQL_Connection.dbExecute(con,"UPDATE factions SET name='?' WHERE id='?'",name, String.valueOf(faction.id));
        SQL_Connection.dbExecute(con,"UPDATE members SET factionname='?' WHERE factionname='?'",name, oldname);
        Scoreboards.RefreshAll();
    }

    public static int getPlayerBalance(Player p) {
        return Integer.parseInt(getMetadata(p, "money"));
    }

    public static void setPlayerBalance(Player p, int amount) {
        setMetadata(p, "money", amount);


    }

    /*public static void setMetadata(Player p, String key, Object data) {
        if (Main.player_cache.containsKey(p.getUniqueId())) {
            PlayerObject obj = Main.player_cache.get(p.getUniqueId());
            obj.setData(key, data);
        }
    }
    public static void setMetadata(UUID uuid, String key, Object data) {
        if (Main.player_cache.containsKey(uuid)) {
            PlayerObject obj = Main.player_cache.get(uuid);
            obj.setData(key, data);
        }
    }

    public static String getMetadata(Player p, String key) {
        if (Main.player_cache.containsKey(p.getUniqueId())) {
            PlayerObject obj = Main.player_cache.get(p.getUniqueId());
            return obj.getData(key);
        } else {
            return "0";
        }
    }

    public static String getMetadata(UUID uuid, String key) {
        if (Main.player_cache.containsKey(uuid)) {
            PlayerObject obj = Main.player_cache.get(uuid);
            return obj.getData(key);
        } else {
            return "0";
        }
    }
    public static Object getRealMetadata(Player p, String key) {
        if (Main.player_cache.containsKey(p)) {
            PlayerObject obj = Main.player_cache.get(p);
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
            PlayerObject obj = Main.player_cache.get(p);
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
                    HCF_Claiming.ClaimAttributes at = HCF_Claiming.ClaimAttributes.NORMAL;
                    if(rs.getString("type").equalsIgnoreCase("protected")){
                        at = HCF_Claiming.ClaimAttributes.PROTECTED;
                    } else if (rs.getString("type").equalsIgnoreCase("koth")) {
                        at = HCF_Claiming.ClaimAttributes.KOTH;
                    }
                    Faction f =  Main.faction_cache.get(rs.getInt("factionid"));
                    if(f != null){
                        HCF_Claiming.Faction_Claim claim = new HCF_Claiming.Faction_Claim(rs.getInt("startX"), rs.getInt("endX"), rs.getInt("startZ"), rs.getInt("endZ"), rs.getInt("factionid"),at,rs.getString("world"));
                        f.addClaim(claim);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getOnlineSize(Faction faction) {
        int counter = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Faction fac = getPlayerFaction(player);
            if(fac != null && fac.id == faction.id) {
                counter++;
            }
        }
        return counter;
    }

    public static boolean hasPermission(Player player, Faction_Rank_Manager.Permissions perm) {
        Faction f = getPlayerFaction(player);
        if (f != null){
            if(f.leader.equalsIgnoreCase(player.getUniqueId().toString())){
                return true;
            }
            Faction_Rank_Manager.Rank rank = f.player_ranks.get(player);
            if(rank.isLeader){
                return true;
            }
            return rank.hasPermission(perm);
        }
        return false;
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
                Faction faction = new Faction(rs.getInt("ID"), rs.getString("name"), rs.getString("leader"), rs.getInt("money"));
                if(isValidJSON(rs.getString("statistics"))){
                    faction.loadFactionHistory(new JSONObject(rs.getString("statistics")));
                }else{
                    faction.loadFactionHistory(faction.assembleFactionHistory());
                }

                Main.faction_cache.put(rs.getInt("ID"), faction);
                Main.nameToFaction.put(rs.getString("name"), faction);
                faction.memberCount = playertools.countMembers(faction);
                faction.DTR = Double.parseDouble(CalculateDTR(faction));
                faction.DTR_MAX = Double.parseDouble(CalculateDTR(faction));
//                if(main_score.getTeam(faction.name) == null)
//                    main_score.registerNewTeam(faction.name).setPrefix(ChatColor.GREEN.toString());
//                else{
//                    main_score.getTeam(faction.name).unregister();
//                    main_score.registerNewTeam(faction.name).setPrefix(ChatColor.GREEN.toString());
//                }
                //System.out.println(main_score.getTeams());
                //Main.sendCmdMessage(faction.name + " UWUUUUUUUUUUUUUUUUUUU");
                if (Main.debug)
                    Main.sendCmdMessage(faction.name + " Prepared");
                if (rs.getString("home") == null)
                    continue;
                Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(rs.getString("home")));
                Location loc = new Location(
                        Bukkit.getWorld(Config.warzone_size.asStr()),
                        Integer.parseInt(map.get("X").toString()),
                        Integer.parseInt(map.get("Y").toString()),
                        Integer.parseInt(map.get("Z").toString()),
                        Integer.parseInt(map.get("YAW").toString()),
                        Integer.parseInt(map.get("PITCH").toString()));
                faction.setHomeLocation(loc);
                /*Main.DTR_REGEN.put(faction.factionid, System.currentTimeMillis() + DTR_REGEN_TIME* 1000L);
                faction.DTR -= Main.DEATH_DTR;*/

            }
            //Check if warzone enabled, and the spawn location is setted
            if(Config.warzone_size.asInt() != 0 && !Main.faction_cache.get(1).claims.isEmpty()) {
                String str = Config.spawn_location.asStr();
                Location spawn = new Location(
                        Bukkit.getWorld(Config.world_name.asStr()),
                        Integer.parseInt(str.split(" ")[0]),
                        Integer.parseInt(str.split(" ")[1]),
                        Integer.parseInt(str.split(" ")[2]),
                        Integer.parseInt(str.split(" ")[3]),
                        Integer.parseInt(str.split(" ")[4])
                );
                Faction f = Main.faction_cache.get(2);
                int warzoneSize = Config.warzone_size.asInt();
                HCF_Claiming.Faction_Claim claim;
                claim = new HCF_Claiming.Faction_Claim(spawn.getBlockX() - warzoneSize, spawn.getBlockX() + warzoneSize, spawn.getBlockZ() - warzoneSize, spawn.getBlockZ() + warzoneSize, 2, HCF_Claiming.ClaimAttributes.SPECIAL,spawn.getWorld().getName());
                f.addClaim(claim);
            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        for(Map.Entry<Integer, Faction> f : Main.faction_cache.entrySet()){
            f.getValue().setupAllies();
        }
    }


    public static HashMap<String, String> getFactionKills(Faction faction) {
        HashMap<String, String> returnHashMap = new HashMap<>();
        for(HCFPlayer p : Main.player_cache.values()){
            if (p.faction == faction)
                returnHashMap.put(p.name, String.valueOf(p.getKills()));

        }
        return returnHashMap;
    }
    //TRASH RAM BOL        <RANK, PLAYERS>
    public static HashMap<String, List<String>> getRankPlayers(Faction faction) {
        HashMap<String, List<String>> returnHashMap = new HashMap<>();
        List<String> alreadyMembers = new ArrayList<>();
        for(HCFPlayer hcf : faction.members){
            alreadyMembers.add(hcf.name);
            returnHashMap.put(hcf.rank.name, alreadyMembers);


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
                for (Map.Entry<Integer, Faction> f : Main.faction_cache.entrySet()) {
                    Integer id = f.getKey();
                    Faction faction = f.getValue();
                    if (id.equals(rank_rs.getInt("faction"))) {
                        Faction_Rank_Manager.Rank rank = new Faction_Rank_Manager.Rank(rank_rs.getInt("ID"), rank_rs.getString("name"));

                        rank.isLeader = rank_rs.getInt("isLeader") == 1;
                        rank.isDefault = rank_rs.getInt("isDefault") == 1;
                        ranks.add(rank);
                        faction.ranks.add(rank);

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
    public static String CalculateDTR(Faction faction){


        double addedDTR = 1.5;
        for(int i = 1; i <= faction.memberCount; i++) {
            if(faction.memberCount % 5 == 0) {
                if (Main.MAX_DTR <= addedDTR + 1)
                    break;
                addedDTR += 1;
            }
            else{
                //5.5 <= 2.0
                if (Main.MAX_DTR <= addedDTR+0.5)
                    break;
                addedDTR += 0.5;
            }
        }
        return String.valueOf(addedDTR);
    }

    public static Location getSpawn() {
        String[] spawnLocs = Config.spawn_location.asStr().split(" ");
        Integer[] ints = getInts(spawnLocs);
        return new Location(Bukkit.getWorld(Config.world_name.asStr()), ints[0], ints[1], ints[2], ints[3], ints[4]);
    }

    public static Integer[] getInts(String[] string) {
        ArrayList<Integer> list = new ArrayList<>();
        for(String str : string) {
            list.add(Integer.parseInt(str));
        }

        return list.toArray(new Integer[list.size()]);
    }
    public static int getFreeFactionId(){
        int largestID = 0;
        ArrayList<Integer> hashKeys = new ArrayList<>(Main.faction_cache.keySet());
        for (Integer hashKey : hashKeys) {
            if (hashKey > largestID) {
                largestID = hashKey;
            }
        }

        return largestID + 1;
    }
    public static int getFreeRankId(){
        int largestID = 0;
        for (Faction_Rank_Manager.Rank hashKey : ranks) {
            if (hashKey.id > largestID) {
                largestID = hashKey.id;
            }
        }

        return largestID + 1;
    }
    public static int createCustomFaction(String name,String leader){
        int largestID = getFreeFactionId();
        Faction faction = new Faction(largestID, name, "", 0);

        Main.faction_cache.put(id, faction);
        Main.factionToname.put(id, faction.name);
        Main.nameToFaction.put(faction.name, faction);
        SQL_Connection.dbExecute(con, "INSERT INTO factions SET ID='?' name='?',leader='?'", String.valueOf(faction.id), name,leader);
        return largestID;
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

        return HCF_Claiming.doOverlap(new_claim_start,new_claim_end,p1,p2);
    }

    public static int getDistanceBetweenPoints2D(HCF_Claiming.Point p1, HCF_Claiming.Point p2) {
        return (Math.abs(p1.x-p2.x) + Math.abs(p1.z-p2.z));
    }
    public static int getDistanceBetweenPoints2D(Location p1,Location p2) {
        return (Math.abs(p1.getBlockX()-p2.getBlockX()) + Math.abs(p1.getBlockZ()-p2.getBlockZ()));
    }
    public static void placeBlockChange(Player p,Location loc){
        if(Main.player_block_changes.containsKey(p.getUniqueId())) {
            List<Location> l = Main.player_block_changes.get(p.getUniqueId());
            l.add(loc);
            Main.player_block_changes.put(p.getUniqueId(),l);
        } else {
            List<Location> l = new ArrayList<>();
            l.add(loc);
            Main.player_block_changes.put(p.getUniqueId(),l);
        }
    }

    public static void prepareKoths() {
        Main.koth_cache.clear();
        HashMap<Integer, Faction> hashMap = Main.faction_cache;

        for (Faction faction : hashMap.values()) {
            for (HCF_Claiming.Faction_Claim claim : faction.claims) {
                if (claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH)) {
                    KOTH.koth_area temp = new KOTH.koth_area(
                            faction,
                            new HCF_Claiming.Point(claim.startX, claim.startZ),
                            new HCF_Claiming.Point(claim.endX, claim.endZ),
                            claim.world
                    );
                    Main.koth_cache.put(faction.name, temp);
                    //eci.put(faction.factioname, temp);
                }
            }
        }
    }

    public static TreeMap<Integer, Faction_Rank_Manager.Rank> sortByPriority(Faction faction){
        HashMap<Integer,Faction_Rank_Manager.Rank> sortedMap = new HashMap<Integer,Faction_Rank_Manager.Rank>();
        for(Faction_Rank_Manager.Rank rank : faction.ranks){
            sortedMap.put(rank.priority,rank);
        }
        return new TreeMap<Integer, Faction_Rank_Manager.Rank>(sortedMap);
    }
    public static boolean isTeammate(Player p,Player p2){
        Faction f1 = getPlayerFaction(p);
        Faction f2 = getPlayerFaction(p2);
        if(f1 == null || f2 == null)
            return false;
        return Objects.equals(f1.id, f2.id);
    }

    private static Map<String, Faction_Rank_Manager.Rank> sortbykey(HashMap map) {
        // TreeMap to store values of HashMap
        Map<String, Faction_Rank_Manager.Rank> sorted = new TreeMap<>(map);

        // Copy all data from hashMap into TreeMap
        sorted.putAll(map);

        // Display the TreeMap which is naturally sorted
        //  hashMap
        return sorted;
    }


    public static String convertLongToTime(long time) {
        time /= 1000;
        long MM = (time % 3600) / 60;
        long SS = time % 60;
        return MM + " minutes " + SS + " seconds";
    }

    /*public static void DrawCircle(Location center,double radius,double smoothness,Effect effect){
        double angle = 0;
        while (angle < 360){
            double radiant_angle = Math.toRadians(angle);
            double angleX = center.getX() + Math.cos(radiant_angle) * radius;
            double angleZ = center.getZ() + Math.sin(radiant_angle) * radius;
            angle = angle + smoothness;
            center.getWorld().playEffect(new Location(center.getWorld(),angleX,center.getY(),angleZ), effect,effect.getId());
        }
    }*/

    public static boolean hasTeam(Player p, String team) {
        return p.getScoreboard().getTeam(team) != null;
    }

    public static Scoreboard getSB(Player p, String team) {
        if(hasTeam(p, team)) {
            return p.getScoreboard();
        } else {
            Scoreboard b = p.getScoreboard();
            b.registerNewTeam(team);
            Team t = b.getTeam(team);
            t.setPrefix("§aPUG");
            p.setPlayerListName("§a" + p.getName());
            t.setDisplayName("§aPUG");
            t.setNameTagVisibility(NameTagVisibility.ALWAYS);
            t.setCanSeeFriendlyInvisibles(true);
            t.setAllowFriendlyFire(false);
            return b;
        }
    }
    public static boolean isValidJSON(String json) {
        try {
            new JSONObject(json);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInStaffDuty(Player p) {
        return Boolean.parseBoolean(getMetadata(p, "adminDuty"));
    }

    public static void sendStaffChat(Messages message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(onlinePlayer.hasPermission("factions.admin")) {
                onlinePlayer.sendMessage(message.language(onlinePlayer).queue());
            }
        }
    }
}
