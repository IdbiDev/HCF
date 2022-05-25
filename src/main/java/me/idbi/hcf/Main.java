package me.idbi.hcf;

import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.CustomFiles.ConfigManager;
import me.idbi.hcf.CustomFiles.MessagesFile;
import me.idbi.hcf.classes.Bard;
import me.idbi.hcf.commands.cmdFunctions.Bank.Faction_DepositBank;
import me.idbi.hcf.commands.cmdFunctions.Faction_Home;
import me.idbi.hcf.tools.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public final class Main extends JavaPlugin {
    // Beállítások configba

    public static String servername;
    public static boolean deathban;
    public static int death_time;
    public static double claim_price_multiplier;
    public static boolean debug;

    private static Connection con;
   public static HashMap<Integer, Faction> faction_cache = new HashMap<>();
    public static HashMap<String, Faction> nameToFaction = new HashMap<>();
    public static HashMap<Integer, String> factionToname = new HashMap<>();
    public static HashMap<Player, Main.Player_Obj> player_cache = new HashMap<>();
    public static HashMap<Integer, Long> DTR_REGEN = new HashMap<>();

    @Override
    public void onEnable() {
        //Todo:
        /*
            Configba:
                - Faction kezdőpénz
                -
            STB:
                - Automata Adatbázis felépítése

         */

        MessagesFile.setup();
        ConfigManager.getManager().setup();
        // Setup variables
        deathban = Boolean.parseBoolean(ConfigLibrary.Deathban.getValue());
        claim_price_multiplier = Double.parseDouble(ConfigLibrary.Claim_price_multiplier.getValue());
        death_time = Integer.parseInt(ConfigLibrary.Death_time_seconds.getValue());
        debug = Boolean.parseBoolean(ConfigLibrary.Debug.getValue());

        con = SQL_Connection.dbConnect(
                ConfigLibrary.DATABASE_HOST.getValue(),
                ConfigLibrary.DATABASE_PORT.getValue(),
                ConfigLibrary.DATABASE_DATABSE.getValue(),
                ConfigLibrary.DATABASE_USER.getValue(),
                ConfigLibrary.DATABASE_PASSWORD.getValue());

        // Variables
        Faction_Home.teleportPlayers = new ArrayList<>();



        //Commandok / Eventek setupolása
        setupEvents.SetupEvents();
        setupCommands.setupCommands();
        //Cache Faction Claims
        playertools.setFactionCache();
        playertools.cacheFactionClaims();
        playertools.LoadRanks();
        //Setup players
        for( Player player : Bukkit.getServer().getOnlinePlayers()) {
            playertools.LoadPlayer(player);
        }
        //Classok
        Bard.setBardItems();
        Misc_Timers.CheckArmors();
        Misc_Timers.addBardEffects();
        Misc_Timers.DTR_Timer();
        Misc_Timers.Bard_Energy();
       // new scoreboard();

        Misc_Timers.AutoSave();
    }

    @Override
    public void onDisable() {
        // Adatbázis kapcsolat leállítása
        SaveAll();
        try {
            con.close();
            con = null;
            faction_cache.clear();
        } catch (Exception ignored) {
        }

    }

    // Egyszerű SQL Connection getter
    public static Connection getConnection(String who){
        if(Main.debug)
            System.out.println(who+" >> GetSQLHandler");
        return con;
    }
    public static ArrayList blacklistedBlocks = new ArrayList() {{
        add(Material.LEVER);
        add(Material.STONE_BUTTON);
        add(Material.WOODEN_DOOR);
        add(Material.WOOD_BUTTON);
        add(Material.WOOD_DOOR);
        add(Material.TRAP_DOOR);
        add(Material.FENCE_GATE);
        add(Material.BIRCH_FENCE_GATE);
        add(Material.BIRCH_DOOR);
        add(Material.SPRUCE_DOOR);
        add(Material.SPRUCE_FENCE_GATE);
        add(Material.JUNGLE_DOOR);
        add(Material.JUNGLE_FENCE_GATE);
        add(Material.ACACIA_DOOR);
        add(Material.ACACIA_FENCE_GATE);
        add(Material.DARK_OAK_DOOR);
        add(Material.DARK_OAK_FENCE_GATE);
        add(Material.CHEST);
        add(Material.TRAPPED_CHEST);
        add(Material.ENDER_CHEST);
        add(Material.HOPPER);
        add(Material.BREWING_STAND);
    }};
    public static void SaveFactions() {
        for(Map.Entry<Integer, Faction> faction : faction_cache.entrySet()){
            Main.Faction f = faction.getValue();
            String[] keys = {
                    "money",
                    "DTR",
                    "name"
            };

            String[] values = {
                    String.valueOf(f.balance),
                    String.valueOf(f.DTR),
                    f.factioname
            };
            if(Main.debug)
                System.out.println("Saving "+ f.factioname + " Faction!");
            SQL_Connection.dbUpdate(con,"factions",keys,values, "ID='"+String.valueOf(faction.getValue().factionid)+"'");
        }
    }
    public static void SavePlayers() {
        for(Map.Entry<Player, Player_Obj> value : player_cache.entrySet()){
            Player_Obj p_Obj = value.getValue();
            Player p = value.getKey();
            if(p == null)
                continue;
            String[] keys = {
                    "faction",
                    "rank",
                    "money",
                    "factionname"
            };
            String[] values = {
                    p_Obj.getData("faction"),
                    p_Obj.getData("rank"),p_Obj.getData("money"),
                    p_Obj.getData("faction")
            };

            if(Main.debug)
                System.out.println("Saving "+ p.getDisplayName() + " Player!");
            // Safe
            SQL_Connection.dbUpdate(con,"factions",keys,values, "uuid='"+p.getUniqueId().toString()+"'");
        }
    }
    public static void SaveAll(){
        SaveFactions();
        SavePlayers();
    }


    // Define classe

    public static class Player_Obj {
        private final HashMap<String, Object> metadata = new HashMap<>();
        public void setData(String key,Object value){
            metadata.put(key,value);
        }
        public String getData(String key){
            try{
                return metadata.get(key).toString();
            }catch (Exception ignored){
                if(Main.debug)
                    Bukkit.getLogger().severe("§4 Error while reading key");
                return null;
            }
        }

        public boolean hasData(String key){
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
        public ArrayList<Player> members;
        public int DTR = 3;
        public inviteManager.factionInvite invites;

        public ArrayList<rankManager.Faction_Rank> ranks = new ArrayList<>();
        public HashMap<Player,rankManager.Faction_Rank> player_ranks = new HashMap<>();

        public Faction(Integer id,String name,String leader,Integer balance) {
            this.factionid = id;
            this.factioname = name;
            this.leader = leader;
            this.invites = new inviteManager.factionInvite(factionid);
            this.balance = balance;
        }

        public void invitePlayer(Player p){
            invites.invitePlayerToFaction(p);
        }
        public void unInvitePlayer(Player p){
            invites.removePlayerFromInvite(p);
        }
        public boolean isPlayerInvited(Player p){
            return invites.isPlayerInvited(p);
        }

        public void addClaim(HCF_Claiming.Faction_Claim claimid){
            try {
                claims.add(claimid);
            } catch (NullPointerException ex) {
                claims = new ArrayList<>();
                claims.add(claimid);
            }
        }
        public HCF_Claiming.Faction_Claim getFactionClaim(Integer id){
            return claims.get(id);
        }

        public void ApplyPlayerRank(Player p,String name){
            for(rankManager.Faction_Rank rank : ranks){
                if(Objects.equals(rank.name, name)){
                    player_ranks.put(p,rank);
                    break;
                }
            }
        }
    }

    public void setDebugMode(String debugMode) {
        debug = debugMode.equalsIgnoreCase("true");
    }
}
