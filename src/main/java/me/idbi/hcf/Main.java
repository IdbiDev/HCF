package me.idbi.hcf;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.idbi.hcf.Bossbar.Bossbar;
import me.idbi.hcf.Commands.FactionCommands.FactionHomeCommand;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.KothRewardsFile;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.AutoKoth;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Scoreboard.CustomTimers;
import me.idbi.hcf.Tools.*;
import me.idbi.hcf.Tools.FactionHistorys.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
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
    public static int worldBorderRadius;
    public static boolean deathban;
    public static int deathbanTime;
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
    public static double deathDTR;
    public static boolean abilitiesLoaded = false;
    public static boolean customEnchantsLoaded = false;
    public static boolean discordLogLoaded = false;
    public static HashMap<String, CustomTimers> customSBTimers;
    public static int DTRREGENTIME;
    public static HashMap<Integer, Faction> factionCache = new HashMap<>();
    public static HashMap<String, Koth.koth_area> kothCache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<UUID, HCFPlayer> playerCache = new HashMap<>();
    public static ArrayList<FactionRankManager.Rank> ranks = new ArrayList<>();
    public static HashMap<Integer, Long> DTRREGEN = new HashMap<>();
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
    public static AutoKoth autoKoth = new AutoKoth();
    private static ConfigManager configManager;
    private static Connection con;


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
        long deltatime = System.currentTimeMillis();
        kothRewardsGUI = new ArrayList<>();
        EOTWStarted = System.currentTimeMillis() / 1000;
        blacklistedRankNames = new ArrayList<>();
        Bossbar.bars = new HashMap<>();
        currentLanguages = new HashMap<>();
        FactionHomeCommand.teleportPlayers = new ArrayList<Player>();

        //MessagesFile.setup();
        //DiscordFile.setup();
        // if (!new File(getDataFolder(), "config.yml").exists()) saveResource("config.yml", true);
        // ConfigManager.getManager().setup();
//        DiscordFile.getDiscord().options().copyDefaults(true);
//        DiscordFile.saveDiscord();
//        saveDefaultConfig();

        configManager = new ConfigManager(this);
        configManager.setup();


        blacklistedRankNames = Config.BlackListedNames.asStrList();

        // Messages
        /*this.manager = new SimpleConfigManager(this);

        this.config = manager.getNewConfig("config.yml", new String[]{" ", "Plugin created by Idbi, koba1" , " "});
        //this.messages = manager.getNewConfig("messages/messages_en.yml");*/

        // Setup variables
        DTRREGENTIME = Config.DTRRegen.asInt() * 1000;
        deathbanTime = Config.Deathban.asInt() * 1000;
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
        deathDTR = Config.DeathDTR.asDouble();
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
        //SetupBot.setup();
        SetupEvents.setupEvents();
        SetupCommands.setupCommands();

        // playertools
        Playertools.setFactionCache();
        Playertools.cacheFactionClaims();
        Playertools.loadRanks();
        Playertools.prepareKoths();
        playerCache.clear();
        Playertools.cacheAll();
        new NameChanger(this);
        // Load online players

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Playertools.loadOnlinePlayer(player);
        }

        // Timers
        MiscTimers.checkArmors();
        MiscTimers.DTRTimer();
        MiscTimers.bardEnergy();
        MiscTimers.potionLimiter();
        MiscTimers.autoSave();
        MiscTimers.KOTHCountdown();
        MiscTimers.cleanupFakeWalls();
        MiscTimers.archerTagEffect();

        SpeedModifiers.asyncCacheBrewingStands();
        SpeedModifiers.speedBoost();

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
        autoKoth.startAutoKoth();


    }

    @EventHandler
    public void onDisableEvent(PluginDisableEvent e) {
        if (e.getPlugin().equals(this)) {
            try {
                for (HCFPlayer hcf : playerCache.values()) {
                    hcf.save();
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) continue;
                    List<Location> copy = Main.playerBlockChanges.get(player.getUniqueId());
                    for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                        Location loc = it.next();
                        player.sendBlockChange(loc, Material.AIR, (byte) 0);
                        if (Main.playerBlockChanges.containsKey(player.getUniqueId())) {
                            List<Location> l = Main.playerBlockChanges.get(player.getUniqueId());
                            it.remove();
                            //l.remove(loc);
                            Main.playerBlockChanges.put(player.getUniqueId(), l);
                        }
                    }

                }
                for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                    integerFactionEntry.getValue().saveFactionData();
                    for (FactionRankManager.Rank rank : integerFactionEntry.getValue().ranks) {
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
        try {
            for (HCFPlayer hcf : playerCache.values()) {
                hcf.save();
            }

            for (Player player : getServer().getOnlinePlayers()) {
                if (!Main.playerBlockChanges.containsKey(player.getUniqueId())) continue;
                List<Location> copy = Main.playerBlockChanges.get(player.getUniqueId());
                for (Iterator<Location> it = copy.iterator(); it.hasNext(); ) {
                    Location loc = it.next();
                    player.sendBlockChange(loc, Material.AIR, (byte) 0);
                    if (Main.playerBlockChanges.containsKey(player.getUniqueId())) {
                        List<Location> l = Main.playerBlockChanges.get(player.getUniqueId());
                        it.remove();
                        //l.remove(loc);
                        Main.playerBlockChanges.put(player.getUniqueId(), l);
                    }
                }

            }
            for (Map.Entry<Integer, Faction> integerFactionEntry : factionCache.entrySet()) {
                integerFactionEntry.getValue().saveFactionData();
                for (FactionRankManager.Rank rank : integerFactionEntry.getValue().ranks) {
                    rank.saveRank();
                }
            }
            con.close();
            con = null;

        } catch (Exception ignored) {
        }

    }


}
