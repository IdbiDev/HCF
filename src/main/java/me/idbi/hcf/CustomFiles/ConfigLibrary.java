package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public enum ConfigLibrary {

    Debug("true"),
    World_name("world"),
    Deathban("false"),
    Death_time_seconds("30"),
    Claim_price_multiplier("1.5"),
    teleport_delay_in_seconds("10"),
    enderpearl_delay("30"),
    Combat_time("30"),
    Archer_tag("30"),
    Spawn_location("0 100 0 0 0"),
    Enchant_xp_cost("20"),
    Faction_default_balance("1000"),
    WORLD_BORDER_DISTANCE("1000"),
    Scoreboard_title("&6HCF+ &7- &eMap &6#1"),

    STUCK_TIMER_DURATION("30"),
    KOTH_TIME("15"),
    DATABASE_HOST("localhost"),
    DATABASE_PORT("3306"),
    DATABASE_DATABSE("meincraft"),
    DATABASE_USER("root"),
    DATABASE_PASSWORD("password"),
    MAX_FACTION_MEMBERS("7");

    private static final Main m = Main.getPlugin(Main.class);
    private String value;
    private final String defaultValue;

    ConfigLibrary(String value) {
        this.value = value;
        this.defaultValue = value;
    }

    public static List<String> getScoreboard() {
        return m.getConfig().getStringList("Scoreboard");
    }

    public static List<Material> blacklistedMaterials() {
        List<String> materialsConfig = m.getConfig().getStringList("Blacklisted-Blocks");
        ArrayList<Material> materials = new ArrayList<>();
        for (String material : materialsConfig) {
            if (Material.matchMaterial(material) != null) {
                materials.add(Material.matchMaterial(material));
            } else {
                Bukkit.getLogger().severe(Messages.NOT_VALID_BLACKLISTED_BLOCKS.getMessage().queue().replace("%material%", material));
            }
        }
        return materials;
    }

    public String getValue() {
        return ChatColor.translateAlternateColorCodes('&', value
                .replace("%newline%", "\n")
                .replace("%prefix%", Messages.PREFIX.getMessage().queue()));
    }

    public String getDefaultValue() {
        return ChatColor.translateAlternateColorCodes('&', defaultValue
                .replace("%newline%", "\n")
                .replace("%prefix%", Messages.PREFIX.getMessage().queue()));
    }

    public void save() {
        FileConfiguration msgs = m.getConfig();

        msgs.set(this.toString(), value);

        m.saveConfig();
    }

    public void load() {
        FileConfiguration msgs = ConfigManager.getManager().getConfig();

        if (msgs == null) return;

        if (msgs.getString(this.toString()) == null) {
            msgs.set(this.toString(), value);
            save();
            return;
        }

        value = msgs.getString(this.toString());
    }
}
