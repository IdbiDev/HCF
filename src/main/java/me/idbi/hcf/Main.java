package me.idbi.hcf;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.idbi.hcf.Bossbar.Bossbar;
import me.idbi.hcf.CustomFiles.*;
import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Discord.SetupBot;
import me.idbi.hcf.Scoreboard.CustomTimers;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.koth.AutoKoth;
import me.idbi.hcf.koth.KOTH;
import me.idbi.hcf.tools.*;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.PlayerObject;
import me.idbi.hcf.tools.Objects.PlayerStatistic;
import me.idbi.hcf.tools.factionhistorys.Nametag.NameChanger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.*;

import static me.idbi.hcf.HCF_Rules.*;



public final class Main extends JavaPlugin implements Listener {
    /*public SimpleConfigManager manager;

    public SimpleConfig config;
    public SimpleConfig messages;*/


    // Beállítások configba
    public static int world_border_radius;
    public static boolean deathban;
    public static int death_time;
    public static double claim_price_multiplier;
    public static boolean debug;
    public static int member_starting_money;
    public static int max_members_pro_faction;
    public static int max_allies_pro_faction;
    public static int stuck_duration;
    public static long EOTWStarted;
    public static long SOTWSTARTED;
    public static boolean EOTW_ENABLED;
    public static boolean SOTW_ENABLED;
    public static int WARZONE_SIZE;
    public static double MAX_DTR;
    public static double DEATH_DTR;
    public static boolean abilities_loaded = false;
    public static boolean customenchants_loaded = false;
    public static boolean discord_log_loaded = false;
    public static HashMap<String, CustomTimers> customSBTimers;
    public static int DTR_REGEN_TIME;
    private static ConfigManager configManager;
    public static HashMap<Integer, Faction> faction_cache = new HashMap<>();

    public static HashMap<String, KOTH.koth_area> koth_cache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<Integer, String> factionToname = new HashMap<>();
    public static HashMap<Player, PlayerObject> player_cache = new HashMap<>();
    public static HashMap<Integer, Long> DTR_REGEN = new HashMap<>();
    public static HashMap<LivingEntity, ArrayList<ItemStack>> saved_items = new HashMap<>();
    public static HashMap<LivingEntity, Long> saved_players = new HashMap<>();

    public static HashMap<UUID, PlayerStatistic> playerStatistics = new HashMap<>();
    public static ArrayList<UUID> death_wait_clear = new ArrayList<>();
    public static ArrayList<String> availableLanguages = new ArrayList<>();
    //public static HashMap<Faction, Scoreboard> teams = new HashMap<>();

    public static HashMap<UUID, List<Location>> player_block_changes = new HashMap<>();
    private static Connection con;
    public static ArrayList<UUID> kothRewardsGUI;

    public static HashMap<UUID, String> currentLanguages;

    public static List<String> blacklistedRankNames;
    public static ProtocolManager protocolManager;
    public static AutoKoth autoKoth = new AutoKoth();

    // Egyszerű SQL Connection getter
    public static Connection getConnection(String who) {
        if (Main.debug)
            System.out.println(who + " >> GetSQLHandler");
        return con;
    }

    public static void SaveFactions() {
        for (Map.Entry<Integer, Faction> faction : faction_cache.entrySet()) {
            Faction f = faction.getValue();
            SQL_Connection.dbExecute(con, "UPDATE factions SET money='?',name='?' WHERE ID = '?'", String.valueOf(f.balance), f.name, String.valueOf(f.id));
        }
    }

    public static void SavePlayers() {
        for (Map.Entry<Player, PlayerObject> value : player_cache.entrySet()) {
            PlayerObject p_Obj = value.getValue();
            Player p = value.getKey();
            if (p == null)
                continue;
            SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?',money='?',factionname='?',name='?' WHERE UUID='?'", p_Obj.getData("factionid"), p_Obj.getData("rank"), p_Obj.getData("money"), p_Obj.getData("faction"),ChatColor.stripColor(p.getName()), p.getUniqueId().toString());

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
        Bossbar.bars = new HashMap<>();
        currentLanguages = new HashMap<>();
        //MessagesFile.setup();
        //DiscordFile.setup();
        // if (!new File(getDataFolder(), "config.yml").exists()) saveResource("config.yml", true);
       // ConfigManager.getManager().setup();
//        DiscordFile.getDiscord().options().copyDefaults(true);
//        DiscordFile.saveDiscord();
//        saveDefaultConfig();


        configManager = new ConfigManager(this);
        configManager.setup();


        blacklistedRankNames = Config.blacklisted_names.asStrList();

        // Messages
        /*this.manager = new SimpleConfigManager(this);

        this.config = manager.getNewConfig("config.yml", new String[]{" ", "Plugin created by Idbi, koba1" , " "});
        this.messages = manager.getNewConfig("messages/messages_en.yml");*/

        // Setup variables
        DTR_REGEN_TIME = Config.dtr_regen.asInt() * 1000;
        death_time = Config.deathban.asInt() * 1000;
        stuck_duration = Config.stuck_timer.asInt() * 1000;
        KOTH.GLOBAL_TIME = Config.koth_length.asInt() * 1000;
        deathban = Config.deathban_enabled.asBoolean();
        member_starting_money = Config.default_balance.asInt();
        claim_price_multiplier = Config.claim_price.asDouble();
        debug = false;
        max_members_pro_faction = Config.max_member.asInt();
        max_allies_pro_faction = Config.max_allies.asInt();
        world_border_radius = Config.world_border_size.asInt();
        WARZONE_SIZE = Config.warzone_size.asInt();
        MAX_DTR = Config.max_dtr.asDouble();
        DEATH_DTR = Config.death_dtr.asDouble();
        con = SQL_Connection.dbConnect(
                Config.host.asStr(),
                Config.port.asStr(),
                Config.database.asStr(),
                Config.username.asStr(),
                Config.password.asStr());
        Plugin multi = getServer().getPluginManager().getPlugin("Multiverse-Core");
        EOTW_ENABLED = false;
        SOTW_ENABLED = false;

        // SQL
        if (con == null)
            return;
        new SQL_Generator();

        // Variables
        player_block_changes = new HashMap<>();
        Faction_Home.teleportPlayers = new ArrayList<>();
        protocolManager = ProtocolLibrary.getProtocolManager();
        //EnchantmentFile.setup();
        AdminTools.InvisibleManager.invisedAdmins = new ArrayList<>();
        customSBTimers = new HashMap<>();

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
            add("&7▍ &ePVP Tag: &6%pvp_timer%");
            add("&7▍ &eSOTW Tag: &6%sotw%");
            add("&7▍ &eEOTW Tag: &6%eotw%");
            add("&7▍ &ePearl: &6%ep_cd%");
            add("&7▍ &eBard energy: &6%bard_energy%");
        }};

        /*getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();*/

        // koth
        KothRewardsFile.setup();

        // setup classes
        SetupBot.setup();
        setupEvents.SetupEvents();
        setupCommands.setupCommands();

        // playertools
        playertools.setFactionCache();
        playertools.cacheFactionClaims();
        playertools.LoadRanks();
        playertools.prepareKoths();
        new NameChanger(this);
        // Load online players
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playertools.LoadPlayer(player);
        }

        // Timers
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

       /* new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    for(String s : BarUtil.getPlayers()) {
                        Player o = Bukkit.getPlayer(s);
                        if(o != null) BarUtil.teleportBar(o);
                    }

                    try {
                        Thread.sleep(1000); // 1000 = 1 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();*/

        // (new BossbarUpdater(this)).runTaskTimer((Plugin)this, 0L, 20L);

        /*
        DISPLAYNAME THINGS!
         */
        //displayTeams.setupAllTeams();

        //SpawnShield.CalcWall();

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
        System.out.println(startMessageInfo);
        autoKoth.startAutoKoth();

    }

    @EventHandler
    public void onDisableEvent(PluginDisableEvent e) {
        if(e.getPlugin().equals(this)) {
            try {
                for(Player player : Bukkit.getOnlinePlayers()){
                    playerStatistics.get(player.getUniqueId()).Save(player);
                    if(!Main.player_block_changes.containsKey(player.getUniqueId())) continue;
                    List<Location> copy = Main.player_block_changes.get(player.getUniqueId());
                    for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                        Location loc = it.next();
                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
                        if (Main.player_block_changes.containsKey(player.getUniqueId())) {
                            List<Location> l = Main.player_block_changes.get(player.getUniqueId());
                            it.remove();
                            //l.remove(loc);
                            Main.player_block_changes.put(player.getUniqueId(), l);
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
                playerStatistics.get(player.getUniqueId()).Save(player);
                if(!Main.player_block_changes.containsKey(player.getUniqueId())) continue;
                List<Location> copy = Main.player_block_changes.get(player.getUniqueId());
                for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                    Location loc = it.next();
                    player.sendBlockChange(loc, Material.AIR, (byte) 0);
                    if (Main.player_block_changes.containsKey(player.getUniqueId())) {
                        List<Location> l = Main.player_block_changes.get(player.getUniqueId());
                        it.remove();
                        //l.remove(loc);
                        Main.player_block_changes.put(player.getUniqueId(), l);
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

    public static void sendCmdMessage(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                Messages.prefix_cmd.queue()+
                msg
        );
    }


}
