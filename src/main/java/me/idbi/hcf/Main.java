package me.idbi.hcf;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.keenant.tabbed.Tabbed;
import lombok.Getter;
import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.Commands.FactionCommands.FactionSetHomeCommand;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.*;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Economy.HCFEconomy;
import me.idbi.hcf.Economy.VaultHook;
import me.idbi.hcf.Koth.AutoKoth;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Scoreboard.BoardManager;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import me.idbi.hcf.Scoreboard.FastBoard.FastBoard;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.Tools.*;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Connection;
import java.util.*;

import static me.idbi.hcf.HCFRules.*;


public final class Main extends JavaPlugin implements Listener {
    /*public SimpleConfigManager manager;

    public SimpleConfig config;
    public SimpleConfig messages;*/


    // Beállítások configba
    @Getter public static TimeZone timeZone;
    public static int serverSlot;
    public static int worldBorderRadius;
    public static boolean deathban;
    public static long deathbanTime;
    public static double claimPriceMultiplier;
    public static int brewingSpeedMultiplier;
    public static int cookSpeedMultiplier;
    public static boolean debug;
    public static int memberStartingMoney;
    public static int maxMembersProFaction;
    public static int maxAlliesProFaction;
    public static int stuckDuration;
    public static long EOTWStarted;
    public static long SOTWStarted;
    public static boolean EOTWENABLED;
    public static boolean SOTWEnabled;
    public static int warzoneSize;
    public static double maxDTR;
    public static long dtrRegenTime;
    public static boolean abilitiesLoaded = false;
    public static boolean customEnchantsLoaded = false;
    public static boolean discordLogLoaded = false;
    public static World endWorld;
    public static World netherWorld;
    public static Location spawnLocation;
    public static Location endSpawnLocation;
    public static HashMap<String, CustomTimers> customSBTimers;
    public static LinkedHashMap<Integer, Faction> factionCache = new LinkedHashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static LinkedHashMap<UUID, HCFPlayer> playerCache = new LinkedHashMap<>();
    public static ArrayList<FactionRankManager.Rank> ranks = new ArrayList<>();
    public static HashMap<LivingEntity, ArrayList<ItemStack>> savedItems = new HashMap<>();
    public static HashMap<LivingEntity, Long> savedPlayers = new HashMap<>();
    public static ArrayList<UUID> deathWaitClear = new ArrayList<>();
    public static ArrayList<String> availableLanguages = new ArrayList<>();
    public static HashMap<UUID, List<Location>> playerBlockChanges = new HashMap<>();
    public static HashMap<UUID, List<Location>> factionMapBlockChanges = new HashMap<>();
    //public static HashMap<Faction, Scoreboard> teams = new HashMap<>();
    public static ArrayList<UUID> kothRewardsGUI;
   // public static HashMap<UUID, String> currentLanguages;
    public static List<String> blacklistedRankNames;
    public static ProtocolManager protocolManager;
    public static HashMap<UUID, Inventory> inventoryRollbacks;
    public static AutoKoth autoKoth = new AutoKoth();
    private static ConfigManager configManager;
    private static Connection con;
    public static MiscTimers miscTimers;
    public static HashMap<UUID, FastBoard> boards;

    public final HashMap<UUID, Double> playerBank = new HashMap<>();
    public static HCFEconomy economyImplementer;
    private VaultHook vaultHook;
    @Getter private static Main instance = null;
    @Getter private Tabbed tabbed;
    @Getter private TabManager tabManager;
    @Getter private BoardManager scoreboardManager;
    @Getter private BungeeChanneling bungeeChanneling;
    public static String lobbyName;
    @Getter private HCFRules rules;

    // Egyszerű SQL Connection getter
    public static Connection getConnection() {
        return con;
    }

    public static void saveFactions() {
        for (Map.Entry<Integer, Faction> faction : factionCache.entrySet()) {
            Faction f = faction.getValue();
            f.saveFactionData();
            //SQL_Connection.dbExecute(con, "UPDATE factions SET money='?',name='?',Allies='?' WHERE ID = '?'", String.valueOf(f.balance), f.name,AlliesEntry, String.valueOf(f.id));
        }
    }

    public static void savePlayers() {
        for (Map.Entry<UUID, HCFPlayer> value : playerCache.entrySet()) {
            HCFPlayer hcf = value.getValue();
            hcf.save();
        }
    }

    public static void saveAll() {
        saveFactions();
        savePlayers();
    }

    public static void sendCmdMessage(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(
                Messages.prefix_cmd.queue() +
                        msg
        );
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnable() {
        instance = this;
        bungeeChanneling = new BungeeChanneling();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungeeChanneling);
        bungeeChanneling.getLobby();

        tabbed = new Tabbed(this);
        /*if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/
        economyImplementer = new HCFEconomy();
        vaultHook = new VaultHook();
        vaultHook.hook();

        serverSlot = Bukkit.getServer().getMaxPlayers();

        endWorld = Bukkit.getWorld(Config.EndName.asStr());
        netherWorld = Bukkit.getWorld(Config.NetherName.asStr());

        long deltatime = System.currentTimeMillis();
        kothRewardsGUI = new ArrayList<>();
        blacklistedRankNames = new ArrayList<>();
        SubCommand.commandCooldowns = new HashMap<>();
        inventoryRollbacks = new HashMap<>();
        boards = new HashMap<>();
        FactionSetHomeCommand.teleportPlayers = new ArrayList<Player>();

        configManager = new ConfigManager(this);
        configManager.setup();

        new BukkitCommandManager();
        BukkitCommandManager.setupMaps();

        blacklistedRankNames = Config.BlackListedNames.asStrList();
        miscTimers = new MiscTimers();

        BoardFile.setup();
        LimitsFile.setup();

        HCFRules rules = new HCFRules();

        this.scoreboardManager = new BoardManager(this);
        this.scoreboardManager.setup();
        ;

        // Setup variables

        dtrRegenTime = Config.DTRRegen.asInt() * 1000L;
        deathbanTime = Config.Deathban.asInt() * 1000L;
        stuckDuration = Config.StuckTimer.asInt() * 1000;
        Koth.GLOBAL_TIME = Config.KoTHDuration.asInt();
        deathban = Config.DeathbanEnable.asBoolean();
        memberStartingMoney = Config.DefaultBalance.asInt();
        claimPriceMultiplier = Config.ClaimPriceMultiplier.asDouble();
        debug = false;
        maxMembersProFaction = Config.MaxMembers.asInt();
        maxAlliesProFaction = Config.MaxAllies.asInt();
        worldBorderRadius = Config.WorldBorderSize.asInt();
        warzoneSize = Config.WarzoneSize.asInt();
        maxDTR = Config.MaxDTR.asDouble();
        cookSpeedMultiplier = Config.CookingSpeedMultiplier.asInt();
        brewingSpeedMultiplier = Config.BrewingSpeedMultiplier.asInt();
        con = SQL_Connection.dbConnect(
                Config.Host.asStr(),
                Config.Port.asStr(),
                Config.Database.asStr(),
                Config.Username.asStr(),
                Config.Password.asStr());
        Plugin multi = getServer().getPluginManager().getPlugin("Multiverse-Core");
        EOTWENABLED = false;
        SOTWEnabled = false;
        timeZone = TimeZone.getTimeZone(Config.Timezone.asStr());

        // SQL
        if (con == null)
            return;
        new SQL_Generator();

        // Variables
        playerBlockChanges = new HashMap<>();
        protocolManager = ProtocolLibrary.getProtocolManager();
        //EnchantmentFile.setup();
        AdminTools.InvisibleManager.invisedAdmins = new ArrayList<>();
        customSBTimers = new HashMap<>();

        // koth
        KothRewardsFile.setup();
        ReclaimFile.setup();
        TabFile.setup();

        this.tabManager = new TabManager(this);

        // setup classes
        //SetupBot.setup();
        SetupEvents.setupEvents();
        SetupCommands.setupCommands();

        HCFServer server = new HCFServer();

        // playertools
        Playertools.setFactionCache();
        Playertools.cacheFactionClaims();
        Playertools.loadRanks();
        playerCache.clear();
        Playertools.cacheAll();
        print(playerCache.size());
        new NameChanger(this);
        // Load online players

        // Timers
        miscTimers.DTRTimer();
        miscTimers.mainRefresher();
        miscTimers.autoSave();
        miscTimers.KOTHCountdown();
        miscTimers.createFakeWalls();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);

        MountainEvent.create();

        SpeedModifiers.asyncCacheBrewingStands();
        SpeedModifiers.speedBoost();

        //displayTeams.setupAllTeams();

        //SpawnShield.CalcWall();

        if (Main.debug) {
            sendCmdMessage("§1Finished loading the plugin! (" + (System.currentTimeMillis() - deltatime) + " ms)");
            if (multi != null)
                sendCmdMessage("§aMultiverse-Core found. Plugin connected to Multiverse-Core\n§aMultiverse-Core version: §a§o" + multi.getDescription().getVersion());

        }
        if (maxMembersProFaction > maxMembersPerFaction) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "The maximum value that can be set is 14. A faction per member should not exceed this!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        if (Bukkit.getWorld(Config.EndName.asStr()) == null) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "End not found!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        if (Bukkit.getWorld(Config.NetherName.asStr()) == null) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "Nether not found!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        if (Bukkit.getWorld(Config.WorldName.asStr()) == null) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "World not found!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        //Double[] dbs = Playertools.getDoubles(Config.SpawnLocation.asStr().split(" "));
        spawnLocation = Playertools.parseLoc(world, Config.SpawnLocation.asStr());
        System.out.println("\n" + startMessage + "\n" + startMessage2);
        System.out.println(startMessageInfo);

        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(spawnLocation);
        autoKoth.startAutoKoth();
        if (Config.WarzoneSize.asInt() != 0) {
            Faction f = Main.factionCache.get(2);
            int warzoneSize = Config.WarzoneSize.asInt();
            Claim claim;
            claim = new Claim(spawnLocation.getBlockX() - warzoneSize, spawnLocation.getBlockX() + warzoneSize, spawnLocation.getBlockZ() - warzoneSize, spawnLocation.getBlockZ() + warzoneSize, 2, ClaimAttributes.SPECIAL, spawnLocation.getWorld().getName());
            f.addClaim(claim);
            claim = new Claim(spawnLocation.getBlockX() - warzoneSize, spawnLocation.getBlockX() + warzoneSize, spawnLocation.getBlockZ() - warzoneSize, spawnLocation.getBlockZ() + warzoneSize, 2, ClaimAttributes.SPECIAL, Config.NetherName.asStr());
            f.addClaim(claim);
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            FastBoard board = new FastBoard(player);
            Main.boards.put(player.getUniqueId(), board);
            Playertools.loadOnlinePlayer(player);
            System.out.println(player);
        }
    }

    private static Economy econ = null;

    public static Location getSpawnLocation(){
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        return world.getSpawnLocation();
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public static Economy getEconomy() {
        return economyImplementer;
    }
    @EventHandler
    public void onDisableEvent(PluginDisableEvent e) {
        if (e.getPlugin().equals(this)) {
            try {
                for (HCFPlayer hcf : playerCache.values()) {
                    hcf.saveSync();
                }
                for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                    integerFactionEntry.getValue().saveFactionDataSync();
                    for (FactionRankManager.Rank rank : integerFactionEntry.getValue().getRanks()) {
                        rank.saveRankSync();
                    }
                    integerFactionEntry.getValue().clearClaims();
                    integerFactionEntry.getValue().setRanks(null);
                    integerFactionEntry.getValue().setMembers(null);
                }
                con.close();//Teccik? eskü igen idk hogy megy e
                //I mean, nem basztam el semmit xD REMÉLEM
                con = null; // az online playereket nem kéne újrabetölteni? Nem, ez disable // szerot már próbáltad xdf? Nem xD
                // fasza, akkor most kipróbálom xd semmi Megy?
                customSBTimers.clear();
                factionCache.clear();
                nameToFaction.clear();
                playerCache.clear();
                ranks.clear();
                savedItems.clear();
                savedPlayers.clear();
                deathWaitClear.clear();
                availableLanguages.clear();
                playerBlockChanges.clear();
                factionMapBlockChanges.clear();
                kothRewardsGUI.clear();
                blacklistedRankNames.clear();
                AdminTools.InvisibleManager.invisedAdmins.clear();
                inventoryRollbacks.clear();
                boards.clear();
                playerBank.clear();
                for(BukkitTask t : miscTimers.tasks){
                    t.cancel();
                }
                miscTimers.tasks.clear();
                HCFServer.getServer().clearMaps();
                HCFRules.getRules().clearLists();
                ConfigManager.getClassConfig().free();
                ConfigManager.getSimpleConfig().free();
                ConfigManager.getMainMessages().free();
                ConfigManager.getEnglishMessages().free();
                ConfigManager.getGUIMessages().free();
                ConfigManager.getGUIEnglishMessages().free();
            } catch (Exception asked) {
                asked.printStackTrace();
            }
        }
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisable() {
        // Adatbázis kapcsolat leállítása
        //SaveAll();

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
            try {
                for (HCFPlayer hcf : playerCache.values()) {
                    hcf.saveSync();
                    hcf.getWaypointPlayer().disable();
                }
                for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                    integerFactionEntry.getValue().saveFactionDataSync();
                    for (FactionRankManager.Rank rank : integerFactionEntry.getValue().getRanks()) {
                        rank.saveRankSync();
                    }
                    integerFactionEntry.getValue().clearClaims();
                    integerFactionEntry.getValue().setRanks(null);
                    integerFactionEntry.getValue().setMembers(null);
                }
                con.close();
                con = null;
                customSBTimers.clear();
                factionCache.clear();
                nameToFaction.clear();
                playerCache.clear();
                ranks.clear();
                savedItems.clear();
                savedPlayers.clear();
                deathWaitClear.clear();
                availableLanguages.clear();
                playerBlockChanges.clear();
                factionMapBlockChanges.clear();
                kothRewardsGUI.clear();
                blacklistedRankNames.clear();
                inventoryRollbacks.clear();
                boards.clear();
                playerBank.clear();
                for(BukkitTask t : miscTimers.tasks){
                    t.cancel();
                }
                miscTimers.tasks.clear();
                HCFServer.getServer().clearMaps();
                HCFRules.getRules().clearLists();
                ConfigManager.getClassConfig().free();
                ConfigManager.getSimpleConfig().free();
                ConfigManager.getMainMessages().free();
                ConfigManager.getEnglishMessages().free();
                ConfigManager.getGUIMessages().free();
                ConfigManager.getGUIEnglishMessages().free();

            } catch (Exception asked) {
                asked.printStackTrace();
                con = null;
                customSBTimers.clear();
                factionCache.clear();
                nameToFaction.clear();
                playerCache.clear();
                ranks.clear();
                savedItems.clear();
                savedPlayers.clear();
                deathWaitClear.clear();
                availableLanguages.clear();
                playerBlockChanges.clear();
                factionMapBlockChanges.clear();
                kothRewardsGUI.clear();
                blacklistedRankNames.clear();
                inventoryRollbacks.clear();
                boards.clear();
                playerBank.clear();
                for(BukkitTask t : miscTimers.tasks){
                    t.cancel();
                }
                miscTimers.tasks.clear();
                HCFServer.getServer().clearMaps();
                HCFRules.getRules().clearLists();
                ConfigManager.getClassConfig().free();
                ConfigManager.getSimpleConfig().free();
                ConfigManager.getMainMessages().free();
                ConfigManager.getEnglishMessages().free();
                ConfigManager.getGUIMessages().free();
                ConfigManager.getGUIEnglishMessages().free();
            }
        }
    public static void print(Object... args){
        for(Object o : args){
            System.out.println(o);
        }
    }


}
