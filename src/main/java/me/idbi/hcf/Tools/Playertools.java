package me.idbi.hcf.Tools;

import me.idbi.hcf.Classes.Classes;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Permissions;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static me.idbi.hcf.Main.*;


public class Playertools {
    private static final Connection con = Main.getConnection();


   /* public static void LoadPlayer(Player player) {

        // Loading player From SQL
        HashMap<String, Object> playerMap = SQL_Connection.dbPoll(con, "SELECT * FROM members WHERE uuid = '?'", player.getUniqueId().toString());
        if (!playerMap.isEmpty()) {
            Main.player_cache.put(player.getUniqueId(), new PlayerObject());
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
            SQL_Connection.dbExecute(con, "INSERT INTO members SET name='?',uuid='?',online=0,money='?'", player.getName(), player.getUniqueId().toString(),Config.default_balance.asInt() + "");
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
            m.save(player);
            //SQL_Connection.dbExecute(con, "INSERT INTO playerstatistics SET uuid='?',StartDate='?',LastLogin='?'", player.getUniqueId().toString(), new Date().toInstant().toString(),new Date().toInstant().toString());
            LoadPlayer(player);
        // }
    }*/

    //OnJoin
    public static void loadOnlinePlayer(Player player) {
        //SQL_Connection.dbExecute(con, "INSERT IGNORE INTO members SET name='?',uuid='?',money='?'", player.getName(), player.getUniqueId().toString(),Config.default_balance.asInt() + "");
        if (!Main.playerCache.containsKey(player.getUniqueId())) {
            SQL_Async.dbExecuteAsync(con, "INSERT INTO members SET name='?',uuid='?',money='?'", player.getName(), player.getUniqueId().toString(), Config.DefaultBalance.asInt() + "");
            HCFPlayer hcf = HCFPlayer.createPlayer(player);
            Main.playerCache.put(hcf.getUUID(), hcf);
            loadOnlinePlayer(player);
            return;
        }


        HCFPlayer hcf = HCFPlayer.getPlayer(player);
        assert hcf != null;
        hcf.createScoreboard(player);
        hcf.setCurrentArea(getUpperClaim(player));
        hcf.setOnline(true);
        Main.playerCache.put(hcf.getUUID(), hcf);
        hcf.join();
        Scoreboards.refresh(player);
        NameChanger.refresh(player);
    }

    public static List<HCFPlayer> getClassesInFaction(Faction faction, Classes classes) {
        List<HCFPlayer> list = new ArrayList<>();
        if (faction == null)
            return list;
        for (HCFPlayer p : faction.getMembers())
            if (p.getPlayerClass().equals(classes)) // cső meleg
                list.add(p);
        return list;

    }

    public static Claiming.Faction_Claim getUpperClaim(Player p) {
        ArrayList<Claiming.Faction_Claim> originalClaims = Claiming.getPlayerArea(p);
        ArrayList<Claiming.Faction_Claim> claims = new ArrayList<>(originalClaims);
        if (originalClaims.size() > 1) {
            for (Claiming.Faction_Claim claim : originalClaims) {
                if (claim.getFaction().equals(Main.factionCache.get(2)))
                    claims.remove(claim);
            }
        }

        // A legkisebb claim legyen felül!!!!

        return claims.isEmpty() ? null : claims.get(0);
    }

    public static Claiming.Faction_Claim getSmaller(Claiming.Faction_Claim claim1, Claiming.Faction_Claim claim2) {
        int claim1X = claim1.getStartX() - claim1.getEndX();
        int claim1Z = claim1.getStartZ() - claim1.getEndZ();
        int blockCount = claim1X * claim1Z;

        int claim2X = claim2.getStartX() - claim2.getEndX();
        int claim2Z = claim2.getStartZ() - claim2.getEndZ();
        int blockCount2 = claim2X * claim2Z;

        if(blockCount > blockCount2) {
            return claim2;
        } else {
            return claim1;
        }
    }

    public static Claiming.Faction_Claim getUpperClaim(Location loc) {
        ArrayList<Claiming.Faction_Claim> originalClaims = Claiming.getClaimsInArea(loc);
        ArrayList<Claiming.Faction_Claim> claims = new ArrayList<>(originalClaims);
        if (originalClaims.size() > 1) {
            for (Claiming.Faction_Claim claim : originalClaims) {
                if (claim.getFaction().equals(Main.factionCache.get(2)))
                    claims.remove(claim);
            }
        }
        return claims.isEmpty() ? null : claims.get(0);
    }

    public static void cacheAll() {
        ResultSet rs = null;
        try {
            //PreparedStatement ps = con.prepareStatement("SELECT * FROM members");
            PreparedStatement ps = con.prepareStatement("SELECT members.uuid,members.name,members.faction,members.rank,members.kills,members.deaths,members.money,members.language,members.statistics,members.lives,deathbans.time FROM `members` LEFT JOIN deathbans ON members.uuid = deathbans.uuid;");
            rs = ps.executeQuery();

            while (rs.next()) {
                cachePlayerSync(UUID.fromString(rs.getString("uuid")), rs);
                Main.sendCmdMessage("Player " + rs.getString("uuid") + " cached!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
            /*try {
                while (rs.next()) {
                    cachePlayerSync(UUID.randomUUID(), rs);
                    Main.sendCmdMessage("Player " + rs.getString("uuid") + " cached!");
                }
            }catch (SQLException ignored){
                System.out.println("WTF");
            }*/
        }
    }

    public static void cachePlayerSync(UUID uuid, ResultSet rs) {
        try {
            HCFPlayer hcf = new HCFPlayer(
                    uuid,
                    rs.getInt("kills"),
                    rs.getInt("deaths"),
                    Main.factionCache.get(rs.getInt("faction")),
                    //Math.toIntExact(Math.round(Main.getEconomy().getBalance(Bukkit.getOfflinePlayer(uuid)))),
                    rs.getInt("money"),
                    new PlayerStatistic(new JSONObject(rs.getString("statistics"))),
                    rs.getString("rank"),
                    rs.getString("language"),
                    rs.getLong("time")
            );
            hcf.setLives(rs.getInt("lives"));
            Main.playerCache.put(hcf.getUUID(), hcf);
            if (hcf.inFaction())
                hcf.getFaction().addMember(hcf);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Reload / Restart / Mindig létezik
//    public static void cachePlayer(UUID uuid) {
//        SQL_Async.dbPollAsync(con, "SELECT * FROM members WHERE uuid='?'", uuid.toString()).thenAcceptAsync(playerMap -> {
//            if (!playerMap.isEmpty()) {
//                HCFPlayer hcf = new HCFPlayer(
//                        uuid,
//                        (int) playerMap.get("kills"),
//                        (int) playerMap.get("deaths"),
//                        Main.factionCache.get((int) playerMap.get("faction")),
//                        (int) playerMap.get("money"),
//                        new PlayerStatistic(new JSONObject(playerMap.get("statistics").toString())),
//                        playerMap.get("rank").toString(),
//                        playerMap.get("language").toString(),0
//                );
//            }
//        });
//    }

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
        return HCFPlayer.getPlayer(p).getFaction();
    }

    public static Faction getPlayerFaction(OfflinePlayer p) {
        return HCFPlayer.getPlayer(p.getUniqueId()).getFaction();
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
            HCFPlayer hcf = HCFPlayer.getPlayer(p);
            if (hcf.getFaction() == null) continue;
            if (hcf.getFaction().getName().equalsIgnoreCase(faction.getName())) {
                return true;
            }
        }
        return false;
    }

    public static Faction getFactionByName(String name) {
        for(Faction f : Main.factionCache.values()){
            if(f.getName().replace("_"," ").equalsIgnoreCase(name) || ChatColor.stripColor(f.getName()).equalsIgnoreCase(ChatColor.stripColor(name)))
                return f;
        }
        return null;
//        if (Main.nameToFaction.containsKey(name)) {
//            return Main.nameToFaction.get(name);
//        }
//        /*for (Map.Entry<Integer, Faction> entry : Main.faction_cache.entrySet()) {
//            if(entry.getValue().name.equalsIgnoreCase(name)) {
//                return entry.getValue();
//            }
//        }*/
//        return null;
    }

    public static ArrayList<Player> getFactionOnlineMembers(Faction faction) {
        ArrayList<Player> players = new ArrayList<Player>();
        if(faction == null) return players;
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (faction.isPlayerInFaction(p)) {
                players.add(p);
            }
        }
        return players;
    }

    public static ArrayList<String> getFactionMembers(int id) {
        ArrayList<String> players = new ArrayList<>();
        Faction f = Main.factionCache.get(id);
        assert null != f;
        for (HCFPlayer member : f.getMembers()) {
            players.add(member.getName());
        }
        return players;
    }

    public static void renameFaction(Faction faction, String name) {
        faction.setName(name);
        Main.factionCache.put(faction.getId(), faction);
        Main.nameToFaction.put(faction.getName(), faction);
        for (HCFPlayer player : faction.getMembers()) {
            player.setFaction(faction);
        }
        //SQL_Connection.dbExecute(con,"UPDATE factions SET name='?' WHERE id='?'", name, String.valueOf(faction.id));
        Scoreboards.refreshAll();
    }

    public static int getPlayerBalance(Player p) {
        return HCFPlayer.getPlayer(p).getMoney();
        // return Integer.parseInt(getMetadata(p, "money"));
    }

    public static void setPlayerBalance(Player p, int amount) {
        HCFPlayer.getPlayer(p).setMoney(amount);
    }

    public static void cacheFactionClaims() {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM claims");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    Claiming.ClaimAttributes at = Claiming.ClaimAttributes.NORMAL;
                    if (rs.getString("type").equalsIgnoreCase("protected")) {
                        at = Claiming.ClaimAttributes.PROTECTED;
                    } else if (rs.getString("type").equalsIgnoreCase("koth")) {
                        at = Claiming.ClaimAttributes.KOTH;
                    }
                    Faction f = Main.factionCache.get(rs.getInt("factionid"));
                    if (f != null) {
                        Claiming.Faction_Claim claim = new Claiming.Faction_Claim(rs.getInt("startX"), rs.getInt("endX"), rs.getInt("startZ"), rs.getInt("endZ"), rs.getInt("factionid"), at, rs.getString("world"));
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
            if (fac != null && fac.getId() == faction.getId()) {
                counter++;
            }
        }
        return counter;
    }

    public static boolean hasPermission(Player player, FactionRankManager.Permissions perm) {
        Faction f = getPlayerFaction(player);
        if (f != null) {
            if (f.getLeader().equalsIgnoreCase(player.getUniqueId().toString())) {
                return true;
            }
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);
            FactionRankManager.Rank rank = hcfPlayer.getRank();
            if (rank.isLeader()) {
                return true;
            }
            return rank.hasPermission(perm);
        }
        return false;
    }

    @Deprecated
    public static double getDTR(int faction) {
        return Main.factionCache.get(faction).getDTR();
    }


    public static void setFactionCache() {
        try {
            Main.factionCache.clear();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM factions");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Faction faction = new Faction(rs.getInt("ID"), rs.getString("name"),
                        Objects.equals(rs.getString("leader"), "null") ? null :  rs.getString("leader"),
                        rs.getInt("money"));
                if (isValidJSON(rs.getString("statistics"))) {
                    faction.loadFactionHistory(new JSONObject(rs.getString("statistics")));
                } else {
                    faction.loadFactionHistory(faction.assembleFactionHistory());
                }
                faction.setPoints(rs.getInt("points"));

                Main.factionCache.put(rs.getInt("ID"), faction);
                Main.nameToFaction.put(rs.getString("name"), faction);

                faction.setDTR(Double.parseDouble(CalculateDTR(faction)));
//                if(main_score.getTeam(faction.name) == null)
//                    main_score.registerNewTeam(faction.name).setPrefix(ChatColor.GREEN.toString());
//                else{
//                    main_score.getTeam(faction.name).unregister();
//                    main_score.registerNewTeam(faction.name).setPrefix(ChatColor.GREEN.toString());
//                }
                //System.out.println(main_score.getTeams());
                //Main.sendCmdMessage(faction.name + " UWUUUUUUUUUUUUUUUUUUU");
                if (Main.debug)
                    Main.sendCmdMessage(faction.getName() + " Prepared");
                if (rs.getString("home") == null || rs.getString("home").equalsIgnoreCase("null"))
                    continue;
                Map<String, Object> map = JsonUtils.jsonToMap(new JSONObject(rs.getString("home")));
                Location loc = new Location(
                        Bukkit.getWorld(Config.WorldName.asStr()),
                        Integer.parseInt(map.get("X").toString()),
                        Integer.parseInt(map.get("Y").toString()),
                        Integer.parseInt(map.get("Z").toString()),
                        Integer.parseInt(map.get("YAW").toString()),
                        Integer.parseInt(map.get("PITCH").toString()));
                int x = loc.getBlockX();
                int z = loc.getBlockZ();
                faction.setHomeLocation(loc.add(x >= 0 ? 0.5 : -0.5, 0.0, z >= 0 ? 0.5 : -0.5));
                /*Main.DTR_REGEN.put(faction.factionid, System.currentTimeMillis() + DTR_REGEN_TIME* 1000L);
                faction.DTR -= Main.DEATH_DTR;*/

            }
            //Check if warzone enabled, and the spawn location is setted

            // ToDo: Setup allies!

        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Integer, Faction> f : Main.factionCache.entrySet()) {
            f.getValue().setupAllies();
        }
    }


    public static HashMap<String, String> getFactionKills(Faction faction) {
        HashMap<String, String> returnHashMap = new HashMap<>();
        for (HCFPlayer p : Main.playerCache.values()) {
            if (p.getFaction() == faction)
                returnHashMap.put(p.getName(), String.valueOf(p.getKills()));

        }
        return returnHashMap;
    }

    //TRASH RAM BOL        <RANK, PLAYERS>
    public static HashMap<String, List<String>> getRankPlayers(Faction faction) {
        HashMap<String, List<String>> returnHashMap = new HashMap<>();
        for (HCFPlayer hcf : faction.getMembers()) {
            List<String> alreadyMembers = returnHashMap.get(hcf.getRank().getName());
            if(alreadyMembers == null)
                alreadyMembers = new ArrayList<>();
            alreadyMembers.add(hcf.getName());
            returnHashMap.put(hcf.getRank().getName(), alreadyMembers);
        }
        return returnHashMap;
    }

    public static void loadRanks() {
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
                for (Map.Entry<Integer, Faction> f : Main.factionCache.entrySet()) {
                    Integer id = f.getKey();
                    Faction faction = f.getValue();
                    if (id.equals(rank_rs.getInt("faction"))) {
                        FactionRankManager.Rank rank = new FactionRankManager.Rank(rank_rs.getInt("ID"), rank_rs.getString("name"), rank_rs.getInt("isDefault") == 1, rank_rs.getInt("isLeader") == 1);
                        rank.setPriority(rank_rs.getInt("priority"));
                        ranks.add(rank);
                        faction.addRank(rank);
                        faction.sortRanks();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String CalculateDTR(Faction faction) {


        double addedDTR = 1.1;
        if(faction.getMemberCount() == 1) return String.valueOf(roundDTR(Config.MaxDTRSolo.asDouble()));
//                   1                   5
        for (int i = 1; i <= faction.getMemberCount(); i++) {
            if( addedDTR + Config.DTRPerPlayer.asDouble() <= maxDTR)
                addedDTR += Config.DTRPerPlayer.asDouble();
            else
                addedDTR = maxDTR;
        }
        return String.valueOf(roundDTR(addedDTR));
    }

    public static Location getSpawn() {
        return spawnLocation;
    }

    public static Integer[] getInts(String[] string) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String str : string) {
            list.add(Integer.parseInt(str));
        }

        return list.toArray(new Integer[list.size()]);
    }

    public static Double[] getDoubles(String[] string) {
        ArrayList<Double> list = new ArrayList<>();
        for (String str : string) {
            list.add(Double.parseDouble(str));
        }

        return list.toArray(new Double[list.size()]);
    }

    public static int getFreeFactionId() {
        int largestID = 0;
        ArrayList<Integer> hashKeys = new ArrayList<>(Main.factionCache.keySet());
        for (Integer hashKey : hashKeys) {
            if (hashKey > largestID) {
                largestID = hashKey;
            }
        }

        return largestID + 1;
    }

    public static int getFreeRankId() {
        int largestID = 0;
        for (FactionRankManager.Rank hashKey : ranks) {
            if (hashKey.getId() > largestID) {
                largestID = hashKey.getId();
            }
        }

        return largestID + 1;
    }

    public static Faction createCustomFaction(String name, String leader) {
        int largestID = getFreeFactionId();
        Faction faction = new Faction(largestID, name, leader, 0);
        Main.factionCache.put(largestID, faction);
        Main.nameToFaction.put(faction.getName(), faction);
        SQL_Connection.dbExecute(con, "INSERT INTO factions SET ID='?', name='?',leader='?'", String.valueOf(faction.getId()), name, leader);
        return faction;
    }

    public static boolean CheckClaimPlusOne(Claiming.Point left_c, Claiming.Point right_c, int diff, Claiming.Point p1, Claiming.Point p2) {
        //Getting the bottom left point
        int minX = Math.min(left_c.getX(), right_c.getX());
        int minZ = Math.min(left_c.getZ(), right_c.getZ());

        //Getting the top right point
        int maxX = Math.max(left_c.getX(), right_c.getX());
        int maxZ = Math.max(left_c.getZ(), right_c.getZ());

        //Creating the new claim
        Claiming.Point new_claim_start = new Claiming.Point(minX - diff, minZ - diff);
        Claiming.Point new_claim_end = new Claiming.Point(maxX + diff, maxZ + diff);

        return Claiming.doOverlap(new_claim_start, new_claim_end, p1, p2);
    }

    public static int getDistanceBetweenPoints2D(Claiming.Point p1, Claiming.Point p2) {
        return (Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getZ() - p2.getZ()));
    }

    public static int getDistanceBetweenPoints2D(Location p1, Location p2) {
        return (Math.abs(p1.getBlockX() - p2.getBlockX()) + Math.abs(p1.getBlockZ() - p2.getBlockZ()));
    }

    public static void placeBlockChange(Player p, Location loc) {
        if (Main.playerBlockChanges.containsKey(p.getUniqueId())) {
            List<Location> l = Main.playerBlockChanges.get(p.getUniqueId());
            l.add(loc);
            Main.playerBlockChanges.put(p.getUniqueId(), l);
        } else {
            List<Location> l = new ArrayList<>();
            l.add(loc);
            Main.playerBlockChanges.put(p.getUniqueId(), l);
        }
    }
    public static TreeMap<Integer, FactionRankManager.Rank> sortByPriority(Faction faction) {
        HashMap<Integer, FactionRankManager.Rank> sortedMap = new HashMap<Integer, FactionRankManager.Rank>();
        for (FactionRankManager.Rank rank : faction.getRanks()) {
            sortedMap.put(rank.getPriority(), rank);
        }

        return new TreeMap<>(sortedMap);
    }

    public static boolean isTeammate(Player p, Player p2) {
        Faction f1 = getPlayerFaction(p);
        Faction f2 = getPlayerFaction(p2);
        if (f1 == null || f2 == null)
            return false;
        return Objects.equals(f1.getId(), f2.getId());
    }

//    private static Map<String, FactionRankManager.Rank> sortbykey(HashMap map) {
//        // TreeMap to store values of HashMap
//        Map<String, FactionRankManager.Rank> sorted = new TreeMap<>(map);
//
//        // Copy all data from hashMap into TreeMap
//        sorted.putAll(map);
//
//        // Display the TreeMap which is naturally sorted
//        //  hashMap
//        return sorted;
//    }

    public static boolean isAlly(Player player1, Player player2) {
        HCFPlayer hcf1 = HCFPlayer.getPlayer(player1);
        HCFPlayer hcf2 = HCFPlayer.getPlayer(player2);
        if (hcf1.getFaction() == null || hcf2.getFaction() == null) return false;
        return hcf1.getFaction().isAlly(hcf2.getFaction());
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
        if (hasTeam(p, team)) {
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
        return HCFPlayer.getPlayer(p).isInDuty();
    }
    public static List<HCFPlayer> getStaffs() {
        List<HCFPlayer> hcfs = new ArrayList<>();
        for (HCFPlayer value : Main.playerCache.values()) {
            if(value.isInDuty()) {
                hcfs.add(value);
            }
        }
        return hcfs;
    }

    public static void sendStaffChat(Messages message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("factions.admin")) {
                onlinePlayer.sendMessage(message.language(onlinePlayer).queue());
            }
        }
    }

    public static String upperFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }


    public static boolean isInWarzone(Location loc) {
        Claiming.Faction_Claim claim = Claiming.sendClaimByXZ(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
        if(claim == null) return false;
        if(claim.getFaction() == null) return false;
        return claim.getFaction().getId() == 2;
    }

    public static boolean isInWarzone(Player p) {
        Claiming.Faction_Claim claim = Claiming.sendClaimByXZ(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockZ());
        if(claim == null) return false;
        if(claim.getFaction() == null) return false;
        return claim.getFaction().getId() == 2;
    }

    public static void cancelSpawnClaim(Player admin) {
        HCFPlayer player = HCFPlayer.getPlayer(admin);
        player.setClaimType(Claiming.ClaimTypes.NONE);
        admin.sendMessage(Messages.faction_claim_decline.language(admin).queue());
        admin.getInventory().remove(Claiming.Wands.claimWand());
    }

    public static boolean isValidName(String name) {
        if(name.length() <= Config.MinNameLength.asInt()){
            return false;
        }
        if(name.length() >= Config.MaxNameLength.asInt()){
            return false;
        }
        for (String badNames : Config.DisabledCharactersInName.asStrList()){
            if(name.toLowerCase().contains(badNames.toLowerCase())){
                return false;
            }
        }
        for (String badNames : Config.BlackListedNames.asStrList()){
            if(name.toLowerCase().contains(badNames.toLowerCase())){
                return false;
            }
        }
        return name.matches("^[a-zA-Z0-9]+$");
        //return name.matches("^\\w{1,32}$");
        //return name.matches("^\\w{3,16}$");
    }


    /**
     *
     * @param loc Location
     * @return {x}, {y}, {z}
     */
    public static String formatLocation(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    /**
     *
     * @param loc
     * @return {x}, {z}
     */
    public static String formatLocationWithoutY(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockZ();
    }

    /**
     *
     * @param DTRNumber
     * @return Rounded DTR number (0.1 -> 0.0 || 0.6 -> 0.5)
     */
    public static double roundDTR(double DTRNumber) {
        DTRNumber = Math.min(DTRNumber, Config.MaxDTR.asDouble());
        if(DTRNumber-Math.floor(DTRNumber) < 0.5)
            DTRNumber = Math.floor(DTRNumber) + 0.1D;
        else
            DTRNumber = Math.floor(DTRNumber) + 0.5D;
        return DTRNumber;
    }

    public static ArrayList<Faction> sortByOnlineMembers() {
        ArrayList<Faction> f = new ArrayList<>(Main.factionCache.values());
        f.removeIf(fac -> Objects.equals(fac.getLeader(), ""));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), null));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), "null"));
        f.sort(Comparator.comparingInt(Faction::countOnlineMembers));
        Collections.reverse(f);

        return f;
    }
    public static ArrayList<Faction> sortByDTR() {
        ArrayList<Faction> f = new ArrayList<>(Main.factionCache.values());
        f.removeIf(fac -> Objects.equals(fac.getLeader(), ""));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), null));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), "null"));
        f.sort(Comparator.comparingDouble(Faction::getDTR));
        Collections.reverse(f);

        return f;
    }
    public static ArrayList<Faction> sortByBalance() {
        ArrayList<Faction> f = new ArrayList<>(Main.factionCache.values());
        f.removeIf(fac -> Objects.equals(fac.getLeader(), ""));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), null));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), "null"));
        f.sort(Comparator.comparingInt(Faction::getBalance));
        Collections.reverse(f);

        return f;
    }
    public static ArrayList<Faction> sortByKills() {
        ArrayList<Faction> f = new ArrayList<>(Main.factionCache.values());
        f.removeIf(fac -> Objects.equals(fac.getLeader(), ""));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), null));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), "null"));
        f.sort(Comparator.comparingInt(Faction::getKills));
        Collections.reverse(f);

        return f;
    }
    public static ArrayList<Faction> sortByPoints() {
        ArrayList<Faction> f = new ArrayList<>(Main.factionCache.values());
        f.removeIf(fac -> Objects.equals(fac.getLeader(), ""));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), null));
        f.removeIf(fac -> Objects.equals(fac.getLeader(), "null"));
        f.sort(Comparator.comparingInt(Faction::getPoints));
        Collections.reverse(f);

        return f;
    }

    /**
     *
     * @param str (x y z yaw pitch)
     * @return Location
     */
    public static Location parseLoc(World world, String str) {
        Double[] ints = getDoubles(str.split(" "));
        return new Location(world,
                ints[0],
                ints[1],
                ints[2],
                Float.parseFloat(ints[3] + ""),
                Float.parseFloat(ints[4] + "")
        );
    }

    /**
     *
     * @param str (x y z)
     * @return
     */
    public static Location parseLoc2(World world, String str) {
        Integer[] ints = getInts(str.split(" "));
        return new Location(world,
                ints[0],
                ints[1],
                ints[2]
        );
    }

    public static boolean isInt(String value) {
        return value.matches("^[0-9]+$");
    }

    public static boolean canDmg(Player victim, Player damager) {
        HCFPlayer hcfVictim = HCFPlayer.getPlayer(victim);
        HCFPlayer hcfDamager = HCFPlayer.getPlayer(damager);
        if (Timers.PVP_TIMER.has(victim)) {
            damager.sendMessage(Messages.cant_damage_while_pvptimer_victim.language(damager).queue());
            return false;
        }
        if (Timers.PVP_TIMER.has(damager)) {
            damager.sendMessage(Messages.cant_damage_while_pvptimer.language(damager).queue());
            return false;
        }
        if (hcfDamager.getFaction() != null && hcfVictim.getFaction() != null) {
            Faction vicFac = hcfVictim.getFaction();
            Faction damFac = hcfDamager.getFaction();
            if (damFac.isAlly(vicFac)) {
                if (!vicFac.hasAllyPermission(damFac, Permissions.FRIENDLY_FIRE) || !damFac.hasAllyPermission(vicFac, Permissions.FRIENDLY_FIRE)) {
                    damager.sendMessage(Messages.teammate_damage.language(damager).queue());
                    return false;
                }
            }
        }
        if (isTeammate(damager, victim) && damager != victim) {
            damager.sendMessage(Messages.teammate_damage.language(damager).queue());
            return false;
        }
        return true;
    }

    public static void refreshPosition(Player p) {
        Claiming.Faction_Claim c = Playertools.getUpperClaim(p);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (player.getCurrentArea() != c) {

            String wilderness = Messages.wilderness.language(p).queue();
            Messages leaveZone = Messages.leave_zone.language(p);
            Messages enteredZone = Messages.entered_zone.language(p);

            p.sendMessage(leaveZone.setZone(player.getLocationFormatted()).queue());
            player.setCurrentArea(c);

            if (c == null) {
                p.sendMessage(enteredZone.language(p).setZone(wilderness).queue());
            } else {
                p.sendMessage(enteredZone.setZone(player.getLocationFormatted()).queue());
            }
            Scoreboards.refresh(p);
        }
    }

    public static Faction getWarzone() {
        return factionCache.get(2);
    }
}
