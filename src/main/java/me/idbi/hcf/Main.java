package me.idbi.hcf;


import me.idbi.hcf.CustomFiles.*;
import me.idbi.hcf.Discord.SetupBot;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.TabManager.PlayerList;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.koth.AutoKoth;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.particles.Shapes;
import me.idbi.hcf.tools.DisplayName.displayTeams;
import me.idbi.hcf.tools.*;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static me.idbi.hcf.HCF_Rules.*;



public final class Main extends JavaPlugin implements Listener {
    // Beállítások configba
    public static int world_border_radius;
    public static boolean deathban;
    public static int death_time;
    public static double claim_price_multiplier;
    public static boolean debug;
    public static int faction_startingmoney;
    public static int max_members_pro_faction;
    public static int stuck_duration;
    public static long EOTWStarted;
    public static int WARZONE_SIZE;
    public static double MAX_DTR;
    public static double DEATH_DTR;
    public static boolean abilities_loaded = false;
    public static boolean customenchants_loaded = false;

    public static int DTR_REGEN_TIME;

    public static HashMap<Integer, Faction> faction_cache = new HashMap<>();

    public static HashMap<String, KOTH.koth_area> koth_cache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<Integer, String> factionToname = new HashMap<>();
    public static HashMap<Player, Main.Player_Obj> player_cache = new HashMap<>();
    public static HashMap<Integer, Long> DTR_REGEN = new HashMap<>();
    public static HashMap<LivingEntity, ArrayList<ItemStack>> saved_items = new HashMap<>();
    public static HashMap<LivingEntity, Long> saved_players = new HashMap<>();
    public static HashMap<Player, PlayerStatistic> playerStatistics = new HashMap<>();
    public static ArrayList<UUID> death_wait_clear = new ArrayList<>();
    //public static HashMap<Main.Faction, Scoreboard> teams = new HashMap<>();

    public static HashMap<Player, List<Location>> player_block_changes = new HashMap<>();
    private static Connection con;
    public static ArrayList<UUID> kothRewardsGUI;

    public static List<String> blacklistedRankNames;
    public static AutoKoth autoKoth = new AutoKoth();

    // Egyszerű SQL Connection getter
    public static Connection getConnection(String who) {
        if (Main.debug)
            System.out.println(who + " >> GetSQLHandler");
        return con;
    }

    public static void SaveFactions() {
        for (Map.Entry<Integer, Faction> faction : faction_cache.entrySet()) {
            Main.Faction f = faction.getValue();
            SQL_Connection.dbExecute(con, "UPDATE factions SET money='?',name='?' WHERE ID = '?'", String.valueOf(f.balance), f.name, String.valueOf(f.id));
        }
    }

    public static void SavePlayers() {
        for (Map.Entry<Player, Player_Obj> value : player_cache.entrySet()) {

            Player_Obj p_Obj = value.getValue();
            Player p = value.getKey();
            if (p == null)
                continue;
            SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?',money='?',factionname='?',name='?' WHERE UUID='?'", p_Obj.getData("factionid"), p_Obj.getData("rank"), p_Obj.getData("money"), p_Obj.getData("faction"),ChatColor.stripColor(p.getName()), p.getUniqueId().toString());
            // Safe
            //SQL_Connection.dbUpdate(con,"factions",keys,values, "uuid='"+p.getUniqueId().toString()+"'");
        }
    }

    public static void SaveAll() {
        SaveFactions();
        SavePlayers();
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnable() {
        long deltatime = System.currentTimeMillis();
        kothRewardsGUI = new ArrayList<>();
        EOTWStarted = System.currentTimeMillis() / 1000;
        blacklistedRankNames = new ArrayList<>();
        blacklistedRankNames = getConfig().getStringList("Blacklisted-rankNames");
        MessagesFile.setup();
        DiscordFile.setup();
        if (!new File(getDataFolder(), "config.yml").exists()) saveResource("config.yml", false);
        ConfigManager.getManager().setup();
//        DiscordFile.getDiscord().options().copyDefaults(true);
//        DiscordFile.saveDiscord();
//        saveDefaultConfig();
        // Setup variables
        deathban = Boolean.parseBoolean(ConfigLibrary.Deathban.getValue());
        claim_price_multiplier = Double.parseDouble(ConfigLibrary.Claim_price_multiplier.getValue());
        death_time = Integer.parseInt(ConfigLibrary.Death_time_seconds.getValue());
        //debug = Boolean.parseBoolean(ConfigLibrary.Debug.getValue());
        debug = false;
        faction_startingmoney = Integer.parseInt(ConfigLibrary.Faction_default_balance.getValue());
        max_members_pro_faction = Integer.parseInt(ConfigLibrary.MAX_FACTION_MEMBERS.getValue());
        DTR_REGEN_TIME = Integer.parseInt(ConfigLibrary.DTR_REGEN_TIME_SECONDS.getValue());
        world_border_radius = Integer.parseInt(ConfigLibrary.WORLD_BORDER_DISTANCE.getValue());
        stuck_duration =  Integer.parseInt(ConfigLibrary.STUCK_TIMER_DURATION.getValue());
        KOTH.GLOBAL_TIME =  Integer.parseInt(ConfigLibrary.KOTH_TIME.getValue()) * 60;
        WARZONE_SIZE =  Integer.parseInt(ConfigLibrary.WARZONE_SIZE.getValue()) * 60;
        MAX_DTR = Double.parseDouble(ConfigLibrary.MAX_DTR.getValue());
        DEATH_DTR = Double.parseDouble(ConfigLibrary.DEATH_DTR.getValue());
        con = SQL_Connection.dbConnect(
                ConfigLibrary.DATABASE_HOST.getValue(),
                ConfigLibrary.DATABASE_PORT.getValue(),
                ConfigLibrary.DATABASE_DATABSE.getValue(),
                ConfigLibrary.DATABASE_USER.getValue(),
                ConfigLibrary.DATABASE_PASSWORD.getValue());
        Plugin multi = getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (con == null)
            return;
        new SQL_Generator();
        // Variables
        player_block_changes = new HashMap<>();
        Faction_Home.teleportPlayers = new ArrayList<>();

        SetupBot.setup();
        //EnchantmentFile.setup();

        // Config
        ArrayList<String> scoreboardList = new ArrayList<String>() {{
            add("&7┌─");
            add("&7│ &eFaction: &6%faction%");
            add("&7│ &eLocation: &6%location%");
            add("&7│ &eMoney: &6$%money%");
            add("&7│ &eClass: &6%class%");
            add("&7└─");
            add("empty");
            add("&7▍ &eStuck: &6%stuck_timer%");
            add("&7▍ &eSpawn Tag: &6%spawntag%");
            add("&7▍ &ePearl: &6%ep_cd%");
            add("&7▍ &eBard energy: &6%bard_energy%");
        }};
//        getConfig().addDefault("Scoreboard", scoreboardList);
//        getConfig().addDefault("Freeze.Ban", true);
//        getConfig().addDefault("Freeze.Reason", "You leaved when you are froze!");
//        getConfig().addDefault("Freeze.BanTimeSeconds", 300);
//        getConfig().addDefault("PvP-Quit.Ban", true);
//        getConfig().addDefault("PvP-Quit.Reason", "You leaved when you are in pvp!");
//        getConfig().addDefault("PvP-Quit.BanTimeSeconds", 900);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
        KothRewardsFile.setup();
        setupEvents.SetupEvents();
        setupCommands.setupCommands();
        playertools.setFactionCache();
        playertools.cacheFactionClaims();
        playertools.LoadRanks();
        playertools.prepareKoths();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playertools.LoadPlayer(player);
        }
        Misc_Timers.CheckArmors();
        Misc_Timers.DTR_Timer();
        Misc_Timers.Bard_Energy();
        Misc_Timers.PotionLimiter();
        Misc_Timers.AutoSave();
        Misc_Timers.KOTH_Countdown();
        Misc_Timers.CleanupFakeWalls();
        Misc_Timers.ArcherTagEffect();
        brewing.Async_Cache_BrewingStands();
        brewing.SpeedBoost();

        /*
        DISPLAYNAME THINGS!
         */
        //displayTeams.setupAllTeams();

        //SpawnShield.CalcWall();
        new Shapes(20);
        if(Main.debug) {
            sendCmdMessage("§1Finished loading the plugin! (" + (System.currentTimeMillis() - deltatime) + " ms)");
            if (multi != null)
                sendCmdMessage("§aMultiverse-Core found. Plugin connected to Multiverse-Core\n§aMultiverse-Core version: §a§o" + multi.getDescription().getVersion());

        }
        if(max_members_pro_faction > maxMembersPerFaction){
            Main.sendCmdMessage(ChatColor.DARK_RED + "The maximum value that can be set is 14. A faction per member should not exceed this!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        System.out.println("\n"+startMessage+"\n"+startMessage2);
        autoKoth.startAutoKoth();

      /*  System.out.println(playerStatistics.get(Bukkit.getPlayer("adbi20014")).startDate);
        System.out.println(playerStatistics.get(Bukkit.getPlayer("adbi20014")).factionHistory);
        System.out.println(playerStatistics.get(Bukkit.getPlayer("adbi20014")).lastLogin);

        System.out.println(playerStatistics.get(Bukkit.getPlayer("kbalu")).startDate);
        System.out.println(playerStatistics.get(Bukkit.getPlayer("kbalu")).factionHistory);
        System.out.println(playerStatistics.get(Bukkit.getPlayer("kbalu")).lastLogin);*/

    }
    @EventHandler
    public void onDisableEvent(PluginDisableEvent e) {
        if(e.getPlugin().equals(Main.getPlugin(Main.class))) {
            try {
                for(Player player : Bukkit.getOnlinePlayers()){
                    playerStatistics.get(player).Save(player);
                    new PlayerList(player, PlayerList.SIZE_FOUR).clearCustomTabs();
                    if(!Main.player_block_changes.containsKey(player)) continue;
                    List<Location> copy = Main.player_block_changes.get(player);
                    for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                        Location loc = it.next();
                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
                        if (Main.player_block_changes.containsKey(player)) {
                            List<Location> l = Main.player_block_changes.get(player);
                            it.remove();
                            //l.remove(loc);
                            Main.player_block_changes.put(player, l);
                        }
                    }

                }
                for (Map.Entry<Integer, Faction> integerFactionEntry : faction_cache.entrySet()) {
                    integerFactionEntry.getValue().saveFactionData();
                    for (Faction_Rank_Manager.Rank rank : integerFactionEntry.getValue().ranks) {
                        rank.saveRank();
                    }
                }
                con.close();
                con = null;

            } catch (Exception asked) {}
        }
    }
    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisable() {
        // Adatbázis kapcsolat leállítása
        //SaveAll();
        try {
            for(Player player : Bukkit.getOnlinePlayers()){
                playerStatistics.get(player).Save(player);
                new PlayerList(player, PlayerList.SIZE_FOUR).clearCustomTabs();
                if(!Main.player_block_changes.containsKey(player)) continue;
                List<Location> copy = Main.player_block_changes.get(player);
                for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                    Location loc = it.next();
                    player.sendBlockChange(loc, Material.AIR, (byte) 0);
                    if (Main.player_block_changes.containsKey(player)) {
                        List<Location> l = Main.player_block_changes.get(player);
                        it.remove();
                        //l.remove(loc);
                        Main.player_block_changes.put(player, l);
                    }
                }

            }
            for (Map.Entry<Integer, Faction> integerFactionEntry : faction_cache.entrySet()) {
                integerFactionEntry.getValue().saveFactionData();
                for (Faction_Rank_Manager.Rank rank : integerFactionEntry.getValue().ranks) {
                    rank.saveRank();
                }
            }
            con.close();
            con = null;

        } catch (Exception ignored) {
        }

    }


    // Define classe

    public void setDebugMode(String debugMode) {
        debug = debugMode.equalsIgnoreCase("true");
    }

    public static class Player_Obj {
        private final HashMap<String, Object> metadata = new HashMap<>();

        public void setData(String key, Object value) {
            metadata.put(key, value);
        }

        public String getData(String key) {
            if (hasData(key))
                return metadata.get(key).toString();
            //Bukkit.getLogger().severe(" Error while reading key:" + key);
            return "";
        }
        public Object getRealData(String key) {
            if (hasData(key))
                return metadata.get(key);
            //Bukkit.getLogger().severe(" Error while reading key:" + key);
            return null;
        }

        public boolean hasData(String key) {
            return metadata.containsKey(key);
        }
    }


    // Készülőben Illetve talán mégse kéne XD
    public static class Faction {

        public int id;

        public String name;

        public String leader;
        public int balance;
        public ArrayList<HCF_Claiming.Faction_Claim> claims = new ArrayList<>();
        public double DTR = 0;
        public double DTR_MAX = 0;
        public long DTR_TIMEOUT = 0L;
        public inviteManager.factionInvite invites;

        public Location homeLocation;

        public ArrayList<Faction_Rank_Manager.Rank> ranks = new ArrayList<>();
        public HashMap<Player, Faction_Rank_Manager.Rank> player_ranks = new HashMap<>();

        public int memberCount;

        //Statistics
        public ArrayList<HistoryEntrys.BalanceEntry> balanceHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.KickEntry> kickHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.JoinLeftEntry> joinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.FactionJoinLeftEntry> factionjoinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.InviteEntry> inviteHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.RankEntry> rankCreateHistory = new ArrayList<>();

        public Faction(int id, String name, String leader, int balance) {
            this.id = id;
            this.name = name;
            this.leader = leader;
            this.invites = new inviteManager.factionInvite();
            this.balance = balance;

        }
        public boolean isPlayerInFaction(Player p){
            return player_ranks.containsKey(p);
        }
        public void loadFactionHistory(JSONObject mainJSON){
            try{
                JSONArray balance_array = mainJSON.getJSONArray("balanceHistory");
                JSONArray kick_array = mainJSON.getJSONArray("kickHistory");
                JSONArray join_array = mainJSON.getJSONArray("joinLeftHistory");
                JSONArray factionjoin_array = mainJSON.getJSONArray("factionjoinLeftHistory");
                JSONArray invite_array = mainJSON.getJSONArray("inviteHistory");
                JSONArray rank_array = mainJSON.getJSONArray("rankCreateHistory");
                if(balance_array.length() > 0){
                    for(int x = 0;x<=balance_array.length()-1;x++) {
                        System.out.println("Balance on lOad: " + balance_array.getJSONObject(x));
                        if(x >= 50){
                            balanceHistory.remove(balanceHistory.size() - 1);
                        }

                        JSONObject obj = balance_array.getJSONObject(x);
                        balanceHistory.add(new HistoryEntrys.BalanceEntry(
                                obj.getInt("amount"),
                                obj.getString("player"),
                                obj.getLong("time")
                        ));
                    }
                }
                if(kick_array.length() > 0){
                    for(int x = 0;x<=kick_array.length()-1;x++) {
                        if(x >= 50){
                            kick_array.remove(kickHistory.size() - 1);
                        }
                        JSONObject obj = kick_array.getJSONObject(x);
                        kickHistory.add(new HistoryEntrys.KickEntry(
                                obj.getString("player"),
                                obj.getString("executor"),
                                obj.getLong("time"),
                                obj.getString("reason")
                        ));
                    }
                }
                if(factionjoin_array.length() > 0){
                    for(int x = 0;x<=factionjoin_array.length()-1;x++) {
                        if(x >= 50){
                            factionjoinLeftHistory.remove(factionjoinLeftHistory.size() - 1);
                        }
                        JSONObject obj = factionjoin_array.getJSONObject(x);
                        factionjoinLeftHistory.add( new HistoryEntrys.FactionJoinLeftEntry(
                                obj.getString("player"),
                                obj.getString("type"),
                                obj.getLong("time")
                        ));
                    }
                }
                if(join_array.length() > 0){
                    for(int x = 0;x<=join_array.length()-1;x++) {
                        if(x >= 50){
                            joinLeftHistory.remove(joinLeftHistory.size() - 1);
                        }
                        JSONObject obj = join_array.getJSONObject(x);
                        joinLeftHistory.add(new HistoryEntrys.JoinLeftEntry(
                                obj.getString("player"),
                                obj.getBoolean("isjoined"),
                                obj.getLong("time")
                        ));
                    }
                }
                if(invite_array.length() > 0){
                    for(int x = 0;x<=invite_array.length()-1;x++) {
                        if(x >= 50){
                            inviteHistory.remove(inviteHistory.size() - 1);
                        }
                        JSONObject obj = invite_array.getJSONObject(x);
                        inviteHistory.add(new HistoryEntrys.InviteEntry(
                                obj.getString("executor"),
                                obj.getString("player"),
                                obj.getLong("time"),
                                obj.getBoolean("isinvited")
                        ));
                    }
                }
                if(rank_array.length() > 0){
                    for(int x = 0;x<=rank_array.length()-1;x++) {
                        if(x >= 50){
                            rankCreateHistory.remove(rankCreateHistory.size());
                        }
                        JSONObject obj = rank_array.getJSONObject(x);
                        rankCreateHistory.add(new HistoryEntrys.RankEntry(
                                obj.getString("rank"),
                                obj.getString("player"),
                                obj.getLong("time"),
                                obj.getString("type")
                        ));
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                loadFactionHistory(assembleFactionHistory());
            }

        }

        public JSONObject assembleFactionHistory(){
            JSONObject main = new JSONObject();
            JSONArray balanceHistory = new JSONArray();
            JSONArray kickHistory = new JSONArray();
            JSONArray joinleftHistory = new JSONArray();
            JSONArray factionJoinLeftHistory = new JSONArray();
            JSONArray inviteHistory = new JSONArray();
            JSONArray rankModifyHistory = new JSONArray();
            for (HistoryEntrys.BalanceEntry balanceEntry : this.balanceHistory) {
                JSONObject balance = new JSONObject();
                balance.put("amount",balanceEntry.amount);
                balance.put("player",balanceEntry.player);
                balance.put("time",balanceEntry.time);
                balanceHistory.put(balance);
                System.out.println("Balance: " + balanceEntry.amount);
            }
            main.put("balanceHistory",balanceHistory);
            for (HistoryEntrys.InviteEntry InviteEntry : this.inviteHistory) {
                JSONObject invite = new JSONObject();
                invite.put("executor",InviteEntry.executor);
                invite.put("player",InviteEntry.player);
                invite.put("time",InviteEntry.time);
                invite.put("isinvited",InviteEntry.isInvited);
                inviteHistory.put(invite);
            }
            main.put("inviteHistory",inviteHistory);
            for (HistoryEntrys.KickEntry KickEntry : this.kickHistory) {
                JSONObject kick = new JSONObject();
                kick.put("executor",KickEntry.executor);
                kick.put("player",KickEntry.player);
                kick.put("time",KickEntry.time);
                kick.put("reason",KickEntry.reason);
                kickHistory.put(kick);
            }
            main.put("kickHistory",kickHistory);
            for (HistoryEntrys.JoinLeftEntry JoinEntry : this.joinLeftHistory) {
                JSONObject join = new JSONObject();
                join.put("player",JoinEntry.player);
                join.put("isjoined",JoinEntry.isJoined);
                join.put("time",JoinEntry.time);
                joinleftHistory.put(join);
            }
            main.put("joinLeftHistory",joinleftHistory);
            for (HistoryEntrys.FactionJoinLeftEntry FactionJoinEntry : this.factionjoinLeftHistory) {
                JSONObject fjoin = new JSONObject();
                fjoin.put("player",FactionJoinEntry.player);
                fjoin.put("type",FactionJoinEntry.type);
                fjoin.put("time",FactionJoinEntry.time);
                factionJoinLeftHistory.put(fjoin);
            }
            main.put("factionjoinLeftHistory",factionJoinLeftHistory);
            for (HistoryEntrys.RankEntry rankEntry : this.rankCreateHistory) {
                JSONObject rank = new JSONObject();
                rank.put("player",rankEntry.player);
                rank.put("type",rankEntry.type);
                rank.put("time",rankEntry.time);
                rank.put("rank",rankEntry.rank);
                rankModifyHistory.put(rank);
            }
            main.put("rankCreateHistory",rankModifyHistory);
            return main;
        }
        public void saveFactionData(){
            SQL_Connection.dbExecute(con,"UPDATE factions SET name='?',money='?',leader='?',`statistics`='?' WHERE ID='?'", name, String.valueOf(balance),leader,assembleFactionHistory().toString(),String.valueOf(id));
        }
        public void invitePlayer(Player p) {
            if(memberCount + 1 > max_members_pro_faction || invites.getInvitedPlayers().size()+1 > HCF_Rules.maxInvitesPerFaction ) {
                p.sendMessage(Messages.MAX_MEMBERS_REACHED.queue());
                return;
            }
            invites.invitePlayerToFaction(p);
        }

        public void unInvitePlayer(Player p) {
            invites.removePlayerFromInvite(p);
        }

        public boolean isPlayerInvited(Player p) {
            return invites.isPlayerInvited(p);
        }

        public void addClaim(HCF_Claiming.Faction_Claim claimid) {
            try {
                claims.add(claimid);
            } catch (NullPointerException ex) {
                claims = new ArrayList<>();
                claims.add(claimid);
                ex.printStackTrace();
            }
        }

        public HCF_Claiming.Faction_Claim getFactionClaim(Integer id) {
            return claims.get(id);
        }

        public void ApplyPlayerRank(Player p, String name) {
            for (Faction_Rank_Manager.Rank rank : ranks) {
                if (Objects.equals(rank.name, name)) {
                    player_ranks.put(p, rank);
                    playertools.setMetadata(p, "rank", rank.name);
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE uuid='?'", rank.name, p.getUniqueId().toString());
                    break;
                }
            }
        }
        public void ApplyPlayerRank(OfflinePlayer p, String name) {
            for (Faction_Rank_Manager.Rank rank : ranks) {
                if (Objects.equals(rank.name, name)) {
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE uuid='?'", rank.name, p.getUniqueId().toString());
                    break;
                }
            }
        }

        public Faction_Rank_Manager.Rank FindRankByName(String name) {
            // Bukkit.broadcastMessage(name);
            //Bukkit.broadcastMessage("Meow " + name.toCharArray().length);
            for (Faction_Rank_Manager.Rank rank : ranks) {
                if (rank.name.equalsIgnoreCase(name)) {
                    return rank;
                }
            }
            return null;
        }

        public Faction_Rank_Manager.Rank getDefaultRank() {
            for (Faction_Rank_Manager.Rank rank : ranks) {
                if (rank.isDefault)
                    return rank;
            }
            Faction_Rank_Manager.Rank default_rank = Faction_Rank_Manager.CreateRank(this, "Default");
            assert default_rank != null;
            default_rank.isDefault = true;
            default_rank.saveRank();
            return default_rank;
        }

        public Faction_Rank_Manager.Rank getLeaderRank() {
            for (Faction_Rank_Manager.Rank rank : ranks) {
                if (rank.isLeader)
                    return rank;
            }
            return null;
        }

        public void setHomeLocation(Location loc) {
            homeLocation = loc;
        }

        public void BroadcastFaction(String message) {
            ArrayList<Player> members = playertools.getFactionOnlineMembers(this);
            for (Player member : members) {
                member.sendMessage(message);
            }
        }
        public void PlayerBroadcast(String message) {
            ArrayList<Player> members = playertools.getFactionOnlineMembers(this);
            for (Player member : members) {
                member.sendMessage();
            }
        }

        public void addPrefixPlayer(Player p) {
            if(1==1){
                return;
            }
            Scoreboard sb = playertools.getSB(p, this.id + "");
            Team team = sb.getTeam(this.id + "");
            team.addEntry(p.getName());
            p.setPlayerListName("§a" + p.getName());
            Bukkit.broadcastMessage(p.getName() + " " + team.hasEntry(p.getName()) + " " + team.getPrefix() + "a");
            p.setScoreboard(sb);
            // System.out.println("Entry Team: " + p.getScoreboard().getEntryTeam(p.getName()));
        }

        private void print(Player p) {
            Scoreboard sb = p.getScoreboard();
            Team team = sb.getTeam(this.id + "");
            Bukkit.broadcastMessage(p.getName() + " Teszt: " + team.hasEntry(p.getName()) + " Prefix: " + team.getPrefix() + "a");

        }

        public void removePrefixPlayer(Player p) {
            if(1==1){
                return;
            }
            Scoreboard sb = playertools.getSB(p, this.id + "");
            Team team = sb.getTeam(this.id + "");
            team.removeEntry(p.getName());
            Bukkit.broadcastMessage(p.getName() + " " + team.hasEntry(p.getName()) + " " + team.getPrefix() + "a");
            p.setScoreboard(sb);
            // System.out.println("Entry Team: " + p.getScoreboard().getEntryTeam(p.getName()));
        }


        public int getKills(){
            int total = 0;
            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM members WHERE faction = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    total += rs.getInt("kills");

                }
            }catch (SQLException ignored)
            {

            }
            return total;
        }
        public int getDeaths(){
            int total = 0;
            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM members WHERE faction = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    total += rs.getInt("deaths");

                }
            }catch (SQLException ignored)
            {

            }
            return total;
        }
        public void refreshDTR(){
            this.DTR_MAX = Double.parseDouble(playertools.CalculateDTR(this));
        }

    }
    public static void sendCmdMessage(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                Messages.PREFIX_CMD.queue()+
                msg
        );
    }
    public static class FactionHistory {
        public final Date joined;
        public Date left;
        public String cause;
        public String name;
        public String lastRole;
        public final int id;
        public FactionHistory(long joined,long left,String cause,String name,String lastRole,int id) {
            this.joined = new Date(joined);
            this.left = new Date(left);
            this.cause = cause;
            this.name = name;
            this.lastRole = lastRole;
            this.id = id;
        }
        public JSONObject toJSON(){
            JSONObject faction = new JSONObject();
            faction.put("name",name);
            faction.put("lastrole",lastRole);
            faction.put("joined",joined.getTime());
            faction.put("left",left.getTime());
            faction.put("cause",cause);
            faction.put("id",id);
            return faction;
        }

    }
    public static class PlayerStatistic {
        //Class Times
        public long TotalBardClassTime;
        public long TotalAssassinClassTime;
        public long TotalArcherClassTime;
        public long TotalMinerClassTime;
        public long TotalClassTime;
        public int MoneySpend;
        public int MoneyEarned;
        public long TimePlayed;
        public long startDate;
        public long lastLogin;
        public int kills;
        public int deaths;

        public ArrayList<FactionHistory> factionHistory = new ArrayList<>();
        public PlayerStatistic(JSONObject mainJSON){
            //FUCK ME
            System.out.println(mainJSON.toString());
            JSONObject ClassTimes = mainJSON.getJSONObject("ClassTimes");
            TotalBardClassTime = Long.parseLong(ClassTimes.get("Bard").toString());
            TotalAssassinClassTime = Long.parseLong(ClassTimes.get("Assassin").toString());
            TotalArcherClassTime = Long.parseLong(ClassTimes.get("Archer").toString());
            TotalMinerClassTime = Long.parseLong(ClassTimes.get("Miner").toString());
            TotalClassTime = Long.parseLong(ClassTimes.get("Total").toString());
            JSONArray array = mainJSON.getJSONArray("FactionHistory");
            if(array.length() > 0){
                for(int x = 0;x<=array.length()-1;x++) {
                    JSONObject obj = array.getJSONObject(x);
                    factionHistory.add(new FactionHistory(
                            obj.getLong("joined"),
                            obj.getLong("left"),
                            obj.getString("cause"),
                            obj.getString("name"),
                            obj.getString("lastrole"),
                            obj.getInt("id")
                    ));
                }
            }
            MoneySpend = Integer.parseInt(String.valueOf(mainJSON.get("MoneySpend")));
            MoneyEarned = Integer.parseInt(String.valueOf(mainJSON.get("MoneyEarned")));
            startDate = Long.parseLong(String.valueOf(mainJSON.get("startDate")));
            lastLogin = Long.parseLong(String.valueOf(mainJSON.get("lastLogin")));
            TimePlayed = Long.parseLong(String.valueOf(mainJSON.get("TimePlayed")));
        }

        public void Save(Player p){
            JSONObject jsonComp = new JSONObject();
            JSONArray factions = new JSONArray();
            //JSONArray ClassTimes = new JSONArray();
            JSONObject classTimes = new JSONObject();
            classTimes.put("Bard",TotalBardClassTime);
            classTimes.put("Assassin",TotalAssassinClassTime);
            classTimes.put("Archer",TotalArcherClassTime);
            classTimes.put("Miner",TotalMinerClassTime);
            classTimes.put("Total",TotalMinerClassTime+TotalArcherClassTime+TotalAssassinClassTime+TotalBardClassTime);

            jsonComp.put("totalFactions", factionHistory.size());
            jsonComp.put("MoneySpend", MoneySpend);
            jsonComp.put("MoneyEarned", MoneyEarned);
            jsonComp.put("TimePlayed", TimePlayed);
            jsonComp.put("startDate", startDate);
            jsonComp.put("lastLogin",new Date().getTime());
            for (FactionHistory f: factionHistory) {
                factions.put(f.toJSON());
            }
            jsonComp.put("FactionHistory", factions);
            jsonComp.put("ClassTimes", classTimes);
            SQL_Connection.dbExecute(con,"UPDATE members SET statistics='?' WHERE uuid='?'",jsonComp.toString(),p.getUniqueId().toString());
        }
    }

}
