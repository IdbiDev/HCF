package me.idbi.hcf.CustomFiles.Configs;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Config {

    default_language("Language", "en", createComment("Sets the default language to the players")),
    host("Database", "na04-sql.pebblehost.com"),
    port("Database", "3306"),
    database("Database", "customer_429941_buildffa"),
    username("Database", "customer_429941_buildffa"),
    password("Database", "N9QPK3U#-fdnXIkDay1h"),
    enderpearl("Cooldowns", 10, createComment("Sets the enderpearl cooldown after thrown.", "Must be in seconds")),
    pvp("Cooldowns", 30, createComment("This will be set if after joining, or respawning.", "Must be in seconds")),
    combattag("Cooldowns", 30, createComment("This will be set if you are hit", "Must be in seconds")),
    archertag("Cooldowns", 30, createComment("This will be set if you are hit by an archer.", "Must be in seconds")),
    golden_apple("Cooldowns", 30, createComment("This will be set if you eat a normal G apple", "Must be in seconds")),
    enchanted_golden_apple("Cooldowns", 3600, createComment("This will be set if you eat an Enchanted G apple", "Must be in seconds")),
    bard_energy("Cooldowns", 3, createComment("This will be set if you use the bard powerup", "Must be in seconds")),
    deathban("Cooldowns", 3600, createComment("This will be set if you die.", "Must be in seconds")),
    home_teleport("Cooldowns", 10, createComment("This will be set if you teleporting home.", "Must be in seconds")),
    stuck_timer("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    logout("Cooldowns", 30, createComment("This will be set if you use the command", "Must be in seconds")),
    koth_length("Cooldowns", 300, createComment("Set the KoTH event length", "Must be in seconds")),
    sotw_length("Cooldowns", 600, createComment("Set the SOTW event length", "Must be in seconds")),
    eotw_length("Cooldowns", 600, createComment("Set the EOTW event length", "Must be in seconds")),
    dtr_regen("Cooldowns", 60, createComment("Must be in seconds")),

    world_name("Default values", "world", createComment("Sets the world, where the HCF server are.")),
    deathban_enabled("Default values", false, createComment("Sets the death-ban.", "Only 'true' or 'false' are accepted!")),
    spawn_location("Default values", "0 64 0 0 0", createComment("Sets the world spawn-point!", "Order: X,Y,Z,Pitch,Yaw (Separation with SPACE instead comma)")),
    claim_price("Default values", 1.5, createComment("(1$/Block) * Multiplier")),
    enchant_cost("Default values", 20, createComment("Enchantment cost. Constant.")),
    default_balance("Default values", 1000, createComment("On the first join, this will be the player money.")),
    world_border_size("Default values", 1000, createComment("This sets the world border radius.")),
    warzone_size("Default values", 500, createComment("This sets the war-zone radius.")),
    max_member("Default values", 7, createComment("Maximum member per faction.")),
    max_allies("Default values", 2, createComment("Maximum allies per faction.")),
    scoreboard_title("Default values", "&6HCF+ &7- &eMap &6#1", createComment("Shows to every player.", "It can contain color codes")),
    max_dtr("Default values", 5.5, createComment("Maximum DTR, that one faction can reach")),
    death_dtr("Default values", 1, createComment("The DTR will be decreased by this number, when a member die.")),
    claiming_wand_name("Claim", "&6Claiming Wand"),
    claiming_wand_lore("Claim", Arrays.asList(
            "&eLeft and right click on the ground",
            "&f➥ to place the positions.",
            " ",
            "&eShift + Right click",
            "&f➥ Accept the claim.",
            " ",
            "&eShift + Left click",
            "&f➥ Discard the claim."
    )),

    teammate_color("Colors", "&a"),
    ally_color("Colors", "&d"),
    enemy_color("Colors", "&c"),
    archer_tagged_color("Colors", "&4"),
    staff_mode_color("Colors", "&b"),

    blacklisted_names(Arrays.asList(
            "dick", "tit", "boobs", "b00bs", "bo0bs", "fuck", "gay"
    ), createComment("These words are blocked in names (Faction,rank etc.)", "No case-sensitive and it will be blocked when the word contains one of them!")),

    default_scoreboard_title("Scoreboards", "&6HCF+ &7- &eMap &6#1"),
    default_scoreboard("Scoreboards", Arrays.asList(
            "&7┌─",
            "&7│ &eFaction: &6%faction%",
            "&7│ &eLocation: &6%location%",
            "&7│ &eMoney: &6$%money%",
            "&7│ &eClass: &6%class%",
            "&7└─",
            "empty",
            "&7▍ &e%customtimers%",
            "&7▍ &eEOTW: &6%eotw%",
            "&7▍ &eSOTW: &6%sotw%",
            "&7▍ &eGapple: &6%gapple_cd%",
            "&7▍ &eOP: &6%opgapple_cd%",
            "&7▍ &eStuck: &6%stuck_timer%",
            "&7▍ &eSpawn Tag: &6%spawntag%",
            "&7▍ &ePearl: &6%ep_cd%",
            "&7▍ &eBard energy: &6%bard_energy%"
    ), createComment("RATATATATATATATATATATATA")),

    admin_scoreboard_title("Scoreboards", "&6HCF+ &7- &eAdmin Duty"),
    admin_scoreboard("Scoreboards", Arrays.asList(
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
