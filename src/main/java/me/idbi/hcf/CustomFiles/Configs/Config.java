package me.idbi.hcf.CustomFiles.Configs;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Config {

    DefaultLanguage("Language", "en", createComment("Sets the default language to the players.")),
    Timezone("Timezone", "Europe/Budapest", createComment("Sets the default timezone on the server. ", "Please use this website if you don't understand the timezone format:","https://code2care.org/pages/java-timezone-list-utc-gmt-offset")),
    Host("Database", "na04-sql.pebblehost.com"),
    Port("Database", "3306"),
    Database("Database", "customer_429941_buildffa"),
    Username("Database", "customer_429941_buildffa"),
    Password("Database", "N9QPK3U#-fdnXIkDay1h"),
    EnderPearl("Cooldowns", 10, createComment("Sets the enderpearl cooldown after thrown.", "Must be in seconds")),
    PvPTimer("Cooldowns", 30, createComment("This will be set if after joining, or respawning.", "Must be in seconds")),
    CombatTag("Cooldowns", 30, createComment("This will be set if you are hit", "Must be in seconds")),
    ArcherTag("Cooldowns", 30, createComment("This will be set if you are hit by an archer.", "Must be in seconds")),
    GolderApple("Cooldowns", 30, createComment("This will be set if you eat a normal G apple", "Must be in seconds")),
    EnchantedGoldenApple("Cooldowns", 3600, createComment("This will be set if you eat an Enchanted G apple", "Must be in seconds")),
    BardEnergy("Cooldowns", 3, createComment("This will be set if you use the bard powerup", "Must be in seconds")),
    Deathban("Cooldowns", 3600, createComment("This will be set if you die.", "Must be in seconds")),
    TeleportHome("Cooldowns", 10, createComment("This will be set if you teleporting home.", "Must be in seconds")),
    StuckTimer("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    Logout("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    KOTHDuration("Cooldowns", 300, createComment("Set the KoTH event length", "Must be in seconds")),
    SOTWDuration("Cooldowns", 600, createComment("Set the SOTW event length", "Must be in seconds")),
    EOTWDuration("Cooldowns", 600, createComment("Set the EOTW event length", "Must be in seconds")),
    DTRRegen("Cooldowns", 60, createComment("Must be in seconds")),

    WorldName("Default values", "world", createComment("Sets the world, where the HCF server are.")),
    DeathbanEnable("Default values", false, createComment("Sets the death-ban.", "Only 'true' or 'false' are accepted!")),
    SpawnLocation("Default values", "0 64 0 0 0", createComment("Sets the world spawn-point!", "Order: X,Y,Z,Pitch,Yaw (Separation with SPACE instead comma)")),
    ClaimPriceMultiplier("Default values", 1.5, createComment("(1$/Block) * Multiplier")),
    EnchantCost("Default values", 20, createComment("Enchantment cost. Constant.")),
    DefaultBalance("Default values", 1000, createComment("On the first join, this will be the player money.")),
    WorldBorderSize("Default values", 1000, createComment("This sets the world border radius.")),
    WarzoneSize("Default values", 500, createComment("This sets the war-zone radius.")),
    MaxMembers("Default values", 7, createComment("Maximum member per faction.")),
    MaxAllies("Default values", 2, createComment("Maximum allies per faction.")),
    MaxDTR("Default values", 5.5, createComment("Maximum DTR, that one faction can reach")),
    DeathDTR("Default values", 1, createComment("The DTR will be decreased by this number, when a member die.")),
    BrewingSpeedMultiplier("Default values", 1, createComment("")),
    CookingSpeedMultiplier("Default values", 1, createComment("")),
    ClaimingWandTitle("Claim", "&6Claiming Wand"),
    ClaimingWandLore("Claim", Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    TeammateColor("Colors", "&a"),
    AllyColor("Colors", "&d"),
    EnemyColor("Colors", "&c"),
    ArcherTagColor("Colors", "&4"),
    StaffModeColor("Colors", "&b"),

    BlackListedNames(Arrays.asList(
            "dick", "tit", "boobs", "b00bs", "bo0bs", "fuck", "gay"
    ), createComment("These words are blocked in names (Faction,rank etc.)", "No case-sensitive and it will be blocked when the word contains one of them!")),

    DefaultScoreboardTitle("Scoreboards", "&6HCF+ &7- &eMap &6#1"),
    DefaultScoreboard("Scoreboards", Arrays.asList(
            "&7┌─",
            "&7│ &eFaction: &6%faction%",
            "&7│ &eLocation: &6%location%",
            "&7│ &eMoney: &6$%money%",
            "&7│ &eClass: &6%class%",
            "&7└─",
            "&7&m----------------------",
            "&7▍ &e%customtimers%",
            "&7▍ &eEOTW: &6%eotw%",
            "&7▍ &eSOTW: &6%sotw%",
            "&7▍ &eGapple: &6%gapple_cd%",
            "&7▍ &eOP: &6%opgapple_cd%",
            "&7▍ &eStuck: &6%stuck_timer%",
            "&7▍ &ePearl: &6%ep_cd%",
            "&7▍ &eEnergy: &6%bard_energy%",
            "&7▍ &eSpawn Tag: &6%spawntag%"
            ), createComment("RATATATATATATATATATATATA")),

    StaffDutyScoreboardTitle("Scoreboards", "&6HCF+ &7- &eStaff Duty"),
    StaffScoreboard("Scoreboards", Arrays.asList(
            "&7&m----------------------",
            "&4* &cStaff Panel",
            "&f* &7Visible: &f%invisible%",
            "&f* &7Chatmode: &f%chat_mode%",
            "&f* &7Players: &f%online_players%",
            "&f* &7TPS: &a%tps%",
            "&7&m----------------------&r",
            "&7▍ &e%customtimers%",
            "&7▍ &eEOTW: &6%eotw%",
            "&7▍ &eSOTW: &6%sotw%",
            "&7▍ &eGapple: &6%gapple_cd%",
            "&7▍ &eOP: &6%opgapple_cd%",
            "&7▍ &eStuck: &6%stuck_timer%",
            "&7▍ &eSpawn Tag: &6%spawntag%",
            "&7▍ &ePearl: &6%ep_cd%",
            "&7▍ &eBard energy: &6%bard_energy%"
    ), createComment("Duty RATATATATA"));

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
                    config.set(section + "." + this, this.value);
                else
                    config.set(section + "." + this, this.value, this.comments);
            }
            save();
            return;
        }

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
}
