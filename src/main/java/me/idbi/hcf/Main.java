package me.idbi.hcf;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.keenant.tabbed.Tabbed;
import lombok.Getter;
import me.idbi.hcf.Bossbar.Bossbar;
import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.Commands.FactionCommands.FactionSetHomeCommand;
import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.BoardFile;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.KothRewardsFile;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.CustomFiles.ReclaimFile;
import me.idbi.hcf.CustomFiles.TabFile;
import me.idbi.hcf.Economy.HCFEconomy;
import me.idbi.hcf.Economy.VaultHook;
import me.idbi.hcf.Koth.AutoKoth;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Scoreboard.CustomTimers;
import me.idbi.hcf.Scoreboard.FastBoard.FastBoard;
import me.idbi.hcf.Scoreboard.BoardManager;
import me.idbi.hcf.TabManager.TabAPIv2.TabAPI;
import me.idbi.hcf.TabManager.TabManager;
import me.idbi.hcf.Tools.*;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.Lag;
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

import java.sql.Connection;
import java.util.*;

import static me.idbi.hcf.HCF_Rules.*;


public final class Main extends JavaPlugin implements Listener {
    /*public SimpleConfigManager manager;

    public SimpleConfig config;
    public SimpleConfig messages;*/


    // Beállítások configba
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
    public static HashMap<String, CustomTimers> customSBTimers;
    public static HashMap<Integer, Faction> factionCache = new HashMap<>();
    public static HashMap<String, HCF_Claiming.Faction_Claim> kothCache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<UUID, HCFPlayer> playerCache = new HashMap<>();
    public static ArrayList<FactionRankManager.Rank> ranks = new ArrayList<>();
    public static HashMap<LivingEntity, ArrayList<ItemStack>> savedItems = new HashMap<>();
    public static HashMap<LivingEntity, Long> savedPlayers = new HashMap<>();
    public static ArrayList<UUID> deathWaitClear = new ArrayList<>();
    public static ArrayList<String> availableLanguages = new ArrayList<>();
    public static HashMap<UUID, List<Location>> playerBlockChanges = new HashMap<>();
    //public static HashMap<Faction, Scoreboard> teams = new HashMap<>();
    public static ArrayList<UUID> kothRewardsGUI;
    public static HashMap<UUID, String> currentLanguages;
    public static List<String> blacklistedRankNames;
    public static ProtocolManager protocolManager;
    public static HashMap<UUID, Inventory> inventoryRollbacks;
    public static AutoKoth autoKoth = new AutoKoth();
    private static ConfigManager configManager;
    private static Connection con;
    private MiscTimers miscTimers;
    public static HashMap<UUID, FastBoard> boards;

    public final HashMap<UUID, Double> playerBank = new HashMap<>();
    public static HCFEconomy economyImplementer;
    private VaultHook vaultHook;
    private static Main instance = null;
    @Getter private Tabbed tabbed;
    @Getter
    private TabManager tabManager;
    @Getter private TabAPI tabAPI;
    @Getter private BoardManager scoreboardManager;


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

            // ToDo: HCFPlayer edited

            /*Player p = value.getKey();
            if (p == null)
                continue;*/
            //SQL_Connection.dbExecute(con, "UPDATE members SET faction='?',rank='?',money='?',factionname='?',name='?' WHERE UUID='?'", p_Obj.getData("factionid"), p_Obj.getData("rank"), p_Obj.getData("money"), p_Obj.getData("faction"),ChatColor.stripColor(p.getName()), p.getUniqueId().toString());

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
        tabbed = new Tabbed(this);

        /*if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }*/
        economyImplementer = new HCFEconomy();
        vaultHook = new VaultHook();
        vaultHook.hook();

        long deltatime = System.currentTimeMillis();
        kothRewardsGUI = new ArrayList<>();
        EOTWStarted = System.currentTimeMillis() / 1000;
        blacklistedRankNames = new ArrayList<>();
        SubCommand.commandCooldowns = new HashMap<>();
        Bossbar.bars = new HashMap<>();
        currentLanguages = new HashMap<>();
        inventoryRollbacks = new HashMap<>();
        boards = new HashMap<>();
        FactionSetHomeCommand.teleportPlayers = new ArrayList<Player>();

        //MessagesFile.setup();
        //DiscordFile.setup();
        // if (!new File(getDataFolder(), "config.yml").exists()) saveResource("config.yml", true);
        // ConfigManager.getManager().setup();
//        DiscordFile.getDiscord().options().copyDefaults(true);
//        DiscordFile.saveDiscord();
//        saveDefaultConfig();

        configManager = new ConfigManager(this);
        configManager.setup();

        new BukkitCommandManager();
        BukkitCommandManager.setupMaps();

        blacklistedRankNames = Config.BlackListedNames.asStrList();
        miscTimers = new MiscTimers();

        BoardFile.setup();

        this.scoreboardManager = new BoardManager(this);
        this.scoreboardManager.setup();

        // Messages
        /*this.manager = new SimpleConfigManager(this);

        this.config = manager.getNewConfig("config.yml", new String[]{" ", "Plugin created by Idbi, koba1" , " "});
        //this.messages = manager.getNewConfig("messages/messages_en.yml");*/

        // Setup variables

        dtrRegenTime = Config.DTRRegen.asInt() * 1000;
        deathbanTime = Config.Deathban.asInt() * 1000L;
        stuckDuration = Config.StuckTimer.asInt() * 1000;
        Koth.GLOBAL_TIME = Config.KOTHDuration.asInt() * 1000;
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
        this.tabAPI = new TabAPI();
        tabAPI.setup();

        // setup classes
        //SetupBot.setup();
        SetupEvents.setupEvents();
        SetupCommands.setupCommands();

        // playertools
        Playertools.setFactionCache();
        Playertools.cacheFactionClaims();
        Playertools.loadRanks();
        playerCache.clear();
        Playertools.cacheAll();
        new NameChanger(this);
        // Load online players

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            FastBoard board = new FastBoard(player);
            Main.boards.put(player.getUniqueId(), board);
            Playertools.loadOnlinePlayer(player);
        }

        // Timers
        miscTimers.DTRTimer();
        miscTimers.mainRefresher();
        miscTimers.autoSave();
        miscTimers.KOTHCountdown();
        miscTimers.cleanupFakeWalls();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);

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
        if (Bukkit.getWorld(Config.WorldName.asStr()) == null) {
            Main.sendCmdMessage(ChatColor.DARK_RED + "World not found!");
            Main.sendCmdMessage(ChatColor.DARK_RED + "Shutting down...");
            Bukkit.getServer().shutdown();
        }
        System.out.println("\n" + startMessage + "\n" + startMessage2);
        System.out.println(startMessageInfo);
        World world = Bukkit.getWorld(Config.WorldName.asStr());
        Integer[] coords = Playertools.getInts(Config.SpawnLocation.asStr().split(" "));
        WorldBorder border = world.getWorldBorder();
        border.setSize(Config.WorldBorderSize.asInt());

        border.setCenter(new Location(world, coords[0], coords[1], coords[2]));
        autoKoth.startAutoKoth();

        /*HCF_Claiming.Faction_Claim spawnClaim = null;
        autoKoth.startAutoKoth();
        SOTW.EnableSOTW();*/

    }

    public static Main getInstance() {
        return instance;
    }

    private static Economy econ = null;

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
                    hcf.save();
                }

//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) continue;
//                    List<Location> copy = Main.playerBlockChanges.get(player.getUniqueId());
//                    for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
//                        Location loc = it.next();
//                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
//                        if (Main.playerBlockChanges.containsKey(player.getUniqueId())) {
//                            List<Location> l = Main.playerBlockChanges.get(player.getUniqueId());
//                            it.remove();
//                            //l.remove(loc);
//                            Main.playerBlockChanges.put(player.getUniqueId(), l);
//                        }
//                    }
//
//                }
                for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                    integerFactionEntry.getValue().saveFactionData();
                    for (FactionRankManager.Rank rank : integerFactionEntry.getValue().getRanks()) {
                        rank.saveRank();
                    }
                }
                con.close();
                con = null;

            } catch (Exception asked) {
            }
        }
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisable() {
        // Adatbázis kapcsolat leállítása
        //SaveAll();
        vaultHook.unhook();
        try {
            for (HCFPlayer hcf : playerCache.values()) {
                hcf.save();
            }

//            for (Player player : getServer().getOnlinePlayers()) {
//                if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) continue;
//                List<Location> copy = Main.playerBlockChanges.get(player.getUniqueId());
//                for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
//                    Location loc = it.next();
//                    player.sendBlockChange(loc, Material.AIR, (byte) 0);
//                    if (Main.playerBlockChanges.containsKey(player.getUniqueId())) {
//                        List<Location> l = Main.playerBlockChanges.get(player.getUniqueId());
//                        it.remove();
//                        //l.remove(loc);
//                        Main.playerBlockChanges.put(player.getUniqueId(), l);
//                    }
//                }
//
//            }
            for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                integerFactionEntry.getValue().saveFactionData();
                for (FactionRankManager.Rank rank : integerFactionEntry.getValue().getRanks()) {
                    rank.saveRank();
                }
            }
            con.close();
            con = null;

        } catch (Exception ignored) {
        }

    }


}
