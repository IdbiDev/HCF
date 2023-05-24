package me.idbi.hcf.CustomFiles.Configs;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public enum Config {

    BungeeCord(true),
    DefaultLanguage("Language", "en", createComment("Sets the default language to the players.")),
    Timezone("Timezone", "Europe/Budapest", createComment("Sets the default timezone on the server. ", "Please use this website if you don't understand the timezone format:","https://code2care.org/pages/java-timezone-list-utc-gmt-offset")),
    Host("Database", "mysqlgame.clans.hu"),
    Port("Database", "3306"),
    Database("Database", "hcfplus917"),
    Username("Database", "hcfplus917"),
    Password("Database", "GUvaJE2E2EQuSup"),
    EnderPearl("Cooldowns", 10, createComment("Sets the enderpearl cooldown after thrown.", "Must be in seconds")),
    PvPTimer("Cooldowns", 30, createComment("This will be set if after joining, or respawning.", "Must be in seconds")),
    CombatTag("Cooldowns", 30, createComment("This will be set if you are hit", "Must be in seconds")),
    ArcherTag("Cooldowns", 30, createComment("This will be set if you are hit by an archer.", "Must be in seconds")),
    GolderApple("Cooldowns", 30, createComment("This will be set if you eat a normal G apple", "Must be in seconds")),
    EnchantedGoldenApple("Cooldowns", 3600, createComment("This will be set if you eat an Enchanted G apple", "Must be in seconds")),
    BardEnergy("Cooldowns", 3, createComment("This will be set if you use the bard powerup", "Must be in seconds")),
    ClassWarmupTime("Cooldowns", 3, createComment("This will be used if you are equipping a class", "Must be in seconds")),
    Deathban("Cooldowns", 3600, createComment("This will be set if you die.", "Must be in seconds")),
    TeleportHome("Cooldowns", 10, createComment("This will be set if you teleporting home.", "Must be in seconds")),
    StuckTimer("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    Logout("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    SOTWDuration("Cooldowns", 600, createComment("Set the SOTW event length", "Must be in seconds")),
    EOTWDuration("Cooldowns", 600, createComment("Set the EOTW event length", "Must be in seconds")),

    //DTRRegen("Cooldowns", 60, createComment("Must be in seconds")),

    WorldName("Default values", "world", createComment("Sets the world, where the HCF server are.")),
    SpawnLocation("Default values", "0.5 64 0.5 0 0", createComment("Sets the world spawn-point!", "Order: X,Y,Z,Pitch,Yaw (Separation with SPACE instead comma)")),
    EndOverworldLocation("Default values", "-9.5 64 -28.5 0 0", createComment("Sets the end, where the HCF end are.")),
    EndName("Default values", "world_the_end", createComment("Sets the end, where the HCF end are.")),
    EndSpawn("Default values", "93.5 59 -2.5 90 0", createComment("Sets the end, where the HCF end are.")),
    NetherName("Default values", "world_nether", createComment("Sets the nether, where the HCF nether are.")),
    NetherSpawn("Default values", "0 0 0 0 0", createComment("Sets the nether, where the HCF nether are.")),
    DeathbanEnable("Default values", false, createComment("Sets the death-ban.", "Only 'true' or 'false' are accepted!")),
    SOTWSpawnEnable("Default values", true, createComment("Enables the spawn tp during SOTW mode.")),

    EnchantCost("Default values", 20, createComment("Enchantment cost. Constant.")),
    DefaultBalance("Default values", 1000, createComment("On the first join, this will be the player money.")),
    DefaultBalanceFaction("Default values", 0, createComment("Upon creating the first faction, this value will be the starting money in the faction.")),
    WorldBorderSize("Default values", 1000, createComment("This sets the world border radius.")),
    WarzoneSize("Default values", 500, createComment("This sets the war-zone radius.")),

    BrewingSpeedMultiplier("Default values", 1, createComment("")),
    CookingSpeedMultiplier("Default values", 1, createComment("")),
    PlayerLoseMoney("Kills", true, createComment("If the player dies, the current balance of the player will be transfered to the killer, or deleted.")),
    AddMoneyOnKill("Kills", 1000, createComment("If the player kills, then this value will deposited into the killer account.")),
    DeathSignTitle("Kills", "&cDeath Sign"),
    DeathSign("Kills", Arrays.asList(
            "&5",
            "&c%victim%",
            "&fslain by",
            "&a%killer%",
            "&f%date%"
    )),
    ClaimingWandTitle("Claim", "&6Claiming Wand"),
    ClaimingWandLore("Claim", Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right Click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left Click",
            "&f➥ Discard the claim."
    )),
    DTRPerPlayer("Faction DTR",0.9,createComment("The amount of DTR added by a player")),
    MaxDTR("Faction DTR",5.5,createComment("The maximum amount of DTR reached by a faction.")),
    MaxDTRSolo("Faction DTR",1.5,createComment("The maximum amount of DTR reached by a solo faction.")),
    DTRRegen("Faction DTR", 60, createComment("DTR regeneration.(Must be in seconds)")),

    DTRLowDTR("Faction DTR", 0.25),
    DTRColorRaidable("Faction DTR", "&4"),
    DTRColorLowDTR("Faction DTR", "&e"),
    DTRColorNormal("Faction DTR", "&a"),
    DTRSymbolFreeze("Faction DTR", "&c■"),
    DTRSymbolRegenerating("Faction DTR", "&a▲"),
    DTRSymbolNormal("Faction DTR", "&a◀"),
    PointStart("Faction Points",0),
    PointPerKill("Faction Points",1),
    PointPerKoth("Faction Points",10),
    EnablePointDecreaseOnDeath("Faction Points",true),
    PointDecreaseOnDeath("Faction Points",1),


    //DeathDTR("Faction DTR", 1, createComment("The DTR will be decreased by this number, when a member die.")),



    OverworldDeathDTR("Faction DTR", 1,createComment("The DTR will be decreased by this number, when a member die.")),
    NetherDeathDTR("Faction DTR", 0.5,createComment("The DTR will be decreased by this number, when a member die.")),
    EndDeathDTR("Faction DTR", 1,createComment("The DTR will be decreased by this number, when a member die.")),


    EnableLeaveFriendly("Faction Settings",false,createComment("This will let the player leave the faction, while the player is inside the faction claim")),
    MaxMembers("Faction Settings", 7, createComment("Maximum member per faction.")),
    MaxAllies("Faction Settings", 2, createComment("Maximum allies per faction.")),
    MinNameLength("Faction Settings",3),
    MaxNameLength("Faction Settings",12),
    DisabledCharactersInName("Faction Settings",Arrays.asList(
            "meow",
            "uwu",
            "kawaii"
    )),

    MustBeConnected("Faction Claim",true),
    MaxClaims("Faction Claim",5),
    MinClaimSize("Faction Claim",4),
    MaxClaimSize("Faction Claim",50),
    EnableFlowingToClaim("Faction Claim",false,createComment("This value will let water flow in a claim. (But, it can still flow inside the claim, outside it will stop when it reaches the claim border)")),
    ClaimPriceMultiplier("Faction Claim", 1.5, createComment("(1$/Block) * Multiplier")),
    UnClaimPriceMultiplier("Faction Claim", 0.5, createComment("(1$/Block) * Multiplier")),
    AllowDamageByWilderness("Faction Claim", false, createComment("Allow liquid flow from wilderness to claimed land")),

    TeammateColor("Colors", "&a"),
    AllyColor("Colors", "&d"),
    EnemyColor("Colors", "&c"),
    ArcherTagColor("Colors", "&4"),
    FocusedTagColor("Colors", "&4"),
    StaffModeColor("Colors", "&b"),

    BlackListedNames(Arrays.asList(
            "dick", "tit", "boobs", "b00bs", "bo0bs", "fuck", "gay"
    ), createComment("These words are blocked in names (Faction, rank etc.)", "No case-sensitive and it will be blocked when the word contains one of them!")),

    MountainEventReset("MountainEvent", 3600, createComment("Set Mountain event reset time", "Must be in seconds")),
    MountainEventMaterial("MountainEvent", Material.GLOWSTONE.name(), createComment("Set Mountain event block type")),
    MountainEventLocation1("MountainEvent", "3 69 -11", createComment("Select 2 location of the o")),
    MountainEventLocation2("MountainEvent", "6 73 -7", createComment("Set Mountain event block type")),

    LunarWaypoints("Waypoints", Arrays.asList(
            "Spawn; world; -39, 60, -9; #FF0000",
            "End; world; -39, 60, -13; #FFFFFF",
            "End Exit; world_the_end; -39, 60, -17; #FFFFFF"
    ), createComment("Syntax: 'name; world; x, y, z; #hexcode'")),
    HomeWaypoint("Waypoints", "Home; #0000FF"),
    RallyWaypoint("Waypoints", "Rally; #FF0000"),
    FocusWaypointColor("Waypoints", "#00FF00"),

    StatTrakEnable("StatTrak", true, createComment("Should we enable weapons stattrak?")),
    StatTrakKillFormat("StatTrak", "&6&lKills&7: &b%kills%", createComment("Stattrak kills counter format")),
    StatTrakKillString("StatTrak", "&e%player% &fwas slain by &e%killer% &6%date%", createComment("Stattrak kill string")),
    StatTrakTrackingItems("StatTrak", Arrays.asList(
            "DIAMOND_SWORD",
            "IRON_SWORD",
            "STONE_SWORD",
            "GOLD_SWORD",
            "WOOD_SWORD"
    ), createComment("Stattrak items")),

    CrowbarMaterial("Crowbar", Material.GOLD_HOE.name()),
    CrowbarName("Crowbar", "&3&lCrowbar"),
    CrowbarLore("Crowbar", Arrays.asList(
            "&5Spawners: &a{&c%scount%&a}",
            "&5Portals: &a{&c%pcount%&a}"
    )),
    CrowbarSpawnerUses("Crowbar", 1),
    CrowbarPortalUses("Crowbar", 6),
    CrowbarDisabledInWarzone("Crowbar", false),

    SignShopEnabled("SignShop", true),
    SignShopTitle_Buy("SignShop", "&a[Buy]"),
    SignShopTitle_Sell("SignShop", "&c[Sell]"),

    ElevatorEnabled("Elevator", true),
    ElevatorTitle("Elevator", "&9[Elevator]"),

    SubClaimTitle("SubClaim", "&b[Subclaim]"),
    SubClaimAllowLowerRank("SubClaim", true, createComment("Allows player which have lower ranks to open chests")),

    KoTHDuration("KoTH", 300, createComment("Set the KoTH event length", "Must be in seconds")),
    AutoKoTHEnabled("KoTH", true, createComment("Starts daily automatically a KoTH","See 'Auto-KoTH' section for more settings")),
    KoTHMinOnline("Auto-KoTH", 10, createComment("Minimum online members to start a KoTH.")),
    KoTHStartDate("Auto-KoTH", Arrays.asList("8:00", "15:00"), createComment("Daily KoTH start date.","When the auto KoTH starts, it will not overwrite the currently going KoTH","The given time is converted to the set timezone!","Make sure,you are using the right Timezone!")),
    KoTHSignTitle("KoTH","&6&nKoTH &r&6Sign"),
    KoTHSign("KoTH",Arrays.asList(
            "&5",
            "&9%koth%",
            "&0capped by",
            "&9%capturer%",
            "%date%"
    ));


    public String section;
    public Object value;
    public String[] comments;

    Config(Object value) {
        this.value = value;
    }

    Config(Object value, String[] comments) {
        this.value = value;
        this.comments = comments;
    }

    Config(String section, Object value) {
        this.value = value;
        this.section = section;
    }

    Config(String section, Object value, String[] comments) {
        this.value = value;
        this.section = section;
        this.comments = comments;
    }

    private static ConfigComments cc() {
        return ConfigComments.get();
    }

    private static String[] createComment(String... args) {
        return args.clone();
    }

    public List<?> asList() {
        return ConfigManager.getSimpleConfig().getList(this.getPath());
    }

    public List<String> asStrList() {
        return ConfigManager.getSimpleConfig().getStringList(this.getPath());
    }

    public List<String> asChatColorList() {
        List<String> cucc = new ArrayList<>();
        for (String s : asStrList()) {
            cucc.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cucc;
    }

    public int asInt() {
        return ConfigManager.getSimpleConfig().getInt(this.getPath());
    }

    public double asDouble() {
        return ConfigManager.getSimpleConfig().getDouble(this.getPath());
    }

    public String asStr() {
        return ChatColor.translateAlternateColorCodes('&', this.value.toString());
    }
    public Material asMaterial() {
        return Material.getMaterial(this.value.toString());
    }

    public boolean asBoolean() {
        return (boolean) this.value;
    }

    public String getPath() {
        String output = "";
        if (this.section != null) {
            output += this.section + ".";
        }
        output += this.toString();
        return output;
    }

    public void load() {
        SimpleConfig config = ConfigManager.getSimpleConfig();

        if (config == null) return;

        if (config.get((this.section == null ? "" : this.section + ".") + this) == null) {
            if (this.section == null) {
                if (this.comments == null)
                    config.set(this.toString(), this.value);
                else
                    config.set(this.toString(), this.value, this.comments);
            } else {
                if (this.comments == null)
                    config.set(section + "." + this.toString(), this.value);
                else
                    config.set(section + "." + this.toString(), this.value, this.comments);
            }
            save();
            return;
        }
        //save();

        this.value = config.get((this.section == null ? "" : this.section + ".") + this);
    }

    public void save() {
        SimpleConfig config = ConfigManager.getSimpleConfig();

        if (this.section == null)
            config.set(this.toString(), this.value);
        else
            config.set(section + "." + this, this.value);

        config.saveConfig();
    }

    public void set(Object newValue) {
        SimpleConfig config = ConfigManager.getSimpleConfig();

        if (this.section == null)
            config.set(this.toString(), newValue);
        else
            config.set(section + "." + this, newValue);

        config.saveConfig();
    }
}
