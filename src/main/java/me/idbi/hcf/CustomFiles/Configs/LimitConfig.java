package me.idbi.hcf.CustomFiles.Configs;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum LimitConfig {

    Speed("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    Jump("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    Strength("Potion Limits", 1,createComment("Set this to 0 to disable the effect.")),
    Regen("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    WaterBreathing("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    FireResistance("Potion Limits", 1,createComment("Set this to 0 to disable the effect.")),
    EmotionalDamage("Potion Limits", 0 ,createComment("(InstantDamage) Set this to 0 to disable the effect.")),
    Poison("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    Weakness("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    Slowness("Potion Limits", 2,createComment("Set this to 0 to disable the effect.")),
    InstantHeal("Potion Limits", 2,createComment("Set this to 0 to disable the effect."));

    public String section;
    public Object value;
    public String[] comments;

    LimitConfig(Object value) {
        this.value = value;
    }

    LimitConfig(Object value, String[] comments) {
        this.value = value;
        this.comments = comments;
    }

    LimitConfig(String section, Object value) {
        this.value = value;
        this.section = section;
    }

    LimitConfig(String section, Object value, String[] comments) {
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
        return ConfigManager.getLimitConfig().getList(this.getPath());
    }

    public List<String> asStrList() {
        return ConfigManager.getLimitConfig().getStringList(this.getPath());
    }

    public List<String> asChatColorList() {
        List<String> cucc = new ArrayList<>();
        for (String s : asStrList()) {
            cucc.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cucc;
    }

    public int asInt() {
        return ConfigManager.getLimitConfig().getInt(this.getPath());
    }

    public double asDouble() {
        return ConfigManager.getLimitConfig().getDouble(this.getPath());
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
        SimpleConfig config = ConfigManager.getLimitConfig();

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

        this.value = config.get((this.section == null ? "" : this.section + ".") + this);
    }

    public void save() {
        SimpleConfig config = ConfigManager.getLimitConfig();

        if (this.section == null)
            config.set(this.toString(), this.value);
        else
            config.set(section + "." + this, this.value);

        config.saveConfig();
    }
}
