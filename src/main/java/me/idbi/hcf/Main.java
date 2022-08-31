package me.idbi.hcf;


import me.idbi.hcf.CustomFiles.*;
import me.idbi.hcf.Discord.SetupBot;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.classes.Bard;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.tools.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static me.idbi.hcf.HCF_Rules.startMessage;
import static me.idbi.hcf.koth.KOTH.startKoth;


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

    public static HashMap<Integer, Faction> faction_cache = new HashMap<>();

    public static HashMap<String, KOTH.koth_area> koth_cache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<Integer, String> factionToname = new HashMap<>();
    public static HashMap<Player, Main.Player_Obj> player_cache = new HashMap<>();
    public static HashMap<Integer, Long> DTR_REGEN = new HashMap<>();
    public static HashMap<LivingEntity, ArrayList<ItemStack>> saved_items = new HashMap<>();
    public static HashMap<LivingEntity, Long> saved_players = new HashMap<>();
    //public static HashMap<Main.Faction, Scoreboard> teams = new HashMap<>();

    public static HashMap<Player, List<Location>> player_block_changes = new HashMap<>();
    private static Connection con;
    public static ArrayList<UUID> kothRewardsGUI;

    // Egyszerű SQL Connection getter
    public static Connection getConnection(String who) {
        if (Main.debug)
            System.out.println(who + " >> GetSQLHandler");
        return con;
    }

    public static void SaveFactions() {
        for (Map.Entry<Integer, Faction> faction : faction_cache.entrySet()) {
            Main.Faction f = faction.getValue();
            SQL_Connection.dbExecute(con, "UPDATE factions SET money='?',DTR='?',name='?' WHERE ID = '?'", String.valueOf(f.balance), String.valueOf(f.DTR), f.factioname, String.valueOf(f.factionid));
        }
    }

    public static void SavePlayers() {
        for (Map.Entry<Player, Player_Obj> value : player_cache.entrySet()) {

            Player_Obj p_Obj = value.getValue();
            Player p = value.getKey();
            if (p == null)
                continue;
            SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?',money='?',factionname='?' WHERE UUID='?'", p_Obj.getData("factionid"), p_Obj.getData("rank"), p_Obj.getData("money"), p_Obj.getData("faction"), p.getUniqueId().toString());
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
        kothRewardsGUI = new ArrayList<>();
        EOTWStarted = System.currentTimeMillis() / 1000;
        long deltatime = System.currentTimeMillis();
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
        debug = Boolean.parseBoolean(ConfigLibrary.Debug.getValue());
        faction_startingmoney = Integer.parseInt(ConfigLibrary.Faction_default_balance.getValue());
        max_members_pro_faction = Integer.parseInt(ConfigLibrary.MAX_FACTION_MEMBERS.getValue());
        world_border_radius = Integer.parseInt(ConfigLibrary.WORLD_BORDER_DISTANCE.getValue());
        stuck_duration =  Integer.parseInt(ConfigLibrary.STUCK_TIMER_DURATION.getValue());
        KOTH.GLOBAL_TIME =  Integer.parseInt(ConfigLibrary.KOTH_TIME.getValue()) * 60;
        WARZONE_SIZE =  Integer.parseInt(ConfigLibrary.WARZONE_SIZE.getValue()) * 60;
        con = SQL_Connection.dbConnect(
                ConfigLibrary.DATABASE_HOST.getValue(),
                ConfigLibrary.DATABASE_PORT.getValue(),
                ConfigLibrary.DATABASE_DATABSE.getValue(),
                ConfigLibrary.DATABASE_USER.getValue(),
                ConfigLibrary.DATABASE_PASSWORD.getValue());
        if (con == null)
            return;
        new SQL_Generator();
        // Variables
        player_block_changes = new HashMap<>();
        Faction_Home.teleportPlayers = new ArrayList<>();

        SetupBot.setup();

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

        // Multiverse detect!!!!!!
        Plugin multi = getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(multi != null) {
            sendCmdMessage("§aMultiverse-Core found. Plugin connected to Multiverse-Core\n§aMultiverse-Core version: §a§o" + multi.getDescription().getVersion());
        }
        //

        KothRewardsFile.setup();

        // Displaynames

        //Commandok / Eventek setupolása
        setupEvents.SetupEvents();
        setupCommands.setupCommands();
        //Cache Faction Claims
        playertools.setFactionCache();
        playertools.cacheFactionClaims();
        playertools.LoadRanks();
        playertools.prepareKoths();
        //Setup players
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playertools.LoadPlayer(player);
        }
        //Classok
        Bard.setBardItems();
        Misc_Timers.CheckArmors();
        Misc_Timers.addBardEffects();
        Misc_Timers.DTR_Timer();
        Misc_Timers.Bard_Energy();
        Misc_Timers.PvpTag();
        //Misc_Timers.pvpTimer();
        Misc_Timers.PotionLimiter();
        Misc_Timers.AutoSave();
        //Misc_Timers.StuckTimers();
        //Misc_Timers.PearlTimer();
        Misc_Timers.KOTH_Countdown();
        Misc_Timers.CleanupFakeWalls();
        brewing.Async_Cache_BrewingStands();
        brewing.SpeedBoost();
        //displayTeams.setupAllTeams();

        sendCmdMessage((System.currentTimeMillis()- deltatime) + "ms");
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisable() {
        // Adatbázis kapcsolat leállítása
        //SaveAll();
        try {
            con.close();
            con = null;
            faction_cache.clear();
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

        public Integer factionid;

        public String factioname;

        public String leader;
        public Integer balance;
        public ArrayList<HCF_Claiming.Faction_Claim> claims = new ArrayList<>();
        public double DTR = 0;
        public inviteManager.factionInvite invites;

        public Location homeLocation;

        public ArrayList<rankManager.Faction_Rank> ranks = new ArrayList<>();
        public HashMap<Player, rankManager.Faction_Rank> player_ranks = new HashMap<>();

        public int memberCount;

        public Faction(Integer id, String name, String leader, Integer balance) {
            this.factionid = id;
            this.factioname = name;
            this.leader = leader;
            this.invites = new inviteManager.factionInvite(factionid);
            this.balance = balance;

        }
        public void invitePlayer(Player p) {
            if(memberCount+1>max_members_pro_faction){
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
            for (rankManager.Faction_Rank rank : ranks) {
                if (Objects.equals(rank.name, name)) {
                    player_ranks.put(p, rank);
                    playertools.setMetadata(p, "rank", rank.name);
                    SQL_Connection.dbExecute(con, "UPDATE members SET rank='?' WHERE uuid='?'", rank.name, p.getUniqueId().toString());
                    break;
                }
            }
        }

        public rankManager.Faction_Rank FindRankByName(String name) {
            for (rankManager.Faction_Rank rank : ranks) {
                if (Objects.equals(rank.name, name)) {
                    return rank;
                }
            }
            return null;
        }

        public rankManager.Faction_Rank getDefaultRank() {
            for (rankManager.Faction_Rank rank : ranks) {
                if (rank.isDefault)
                    return rank;
            }
            return null;
        }

        public rankManager.Faction_Rank getLeaderRank() {
            for (rankManager.Faction_Rank rank : ranks) {
                if (rank.isLeader)
                    return rank;
            }
            return null;
        }

        public void setHomeLocation(Location loc) {
            homeLocation = loc;
        }

        public void BroadcastFaction(String message) {
            Player[] members = playertools.getFactionOnlineMembers(factioname);
            for (Player member : members) {
                member.sendMessage(message);
            }
        }

        public int getKills(){
            int total = 0;
            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM members WHERE faction = ?");
                ps.setInt(1,factionid);
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
                ps.setInt(1,factionid);
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
            this.DTR = Double.parseDouble(playertools.CalculateDTR(this));
        }

    }
    public static void sendCmdMessage(String msg){
        Bukkit.getServer().getConsoleSender().sendMessage(
                Messages.PREFIX_CMD.queue()+
                msg
        );
    }
}
