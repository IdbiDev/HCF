package me.idbi.hcf.CustomFiles.Configs;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public enum ClassConfig {

    ArcherEnabled("Archer",true,createComment("Enables the class")),
    ArcherTagEnabled("Archer",true,createComment("Enables the archertag.")),
    ArcherTagDamageAmplifier("Archer",100.0,createComment("Sets the damage amplifier.(% value)")),
    MaxArcherInFaction("Archer",-1,createComment("Max archer class users per faction. Set to -1 to disable the limit.")),

    BardEnabled("Bard",true,createComment("Enables the class")),
    BoostEnabled("Bard",true,createComment("Enables the right-click boost.(Amplifies the held effect)")),
    SimpleBoostEnabled("Bard",true,createComment("Enabled the hotbar effect boost. (Simple area boost effect.)")),
    MaxBardEnergy("Bard",100,createComment("Sets the maximum amount bard energy. Please do not use minus value :)")),
    BardEnergyMultiplier("Bard",1.0,createComment("The amount of bard energy given per second.")),
    MaxBardInFaction("Bard",-1,createComment("Max bard class users per faction. Set to -1 to disable the limit.")),
    UseNewBardSystem("Bard",true,createComment("Use the new bard system: You can use your hotbar to hold all the effects.","Setting this value to false will disable this feature and it will going to use the old bard system: (Giving the effect in your ha)")),
    UseEffects("Bard",true,createComment("This value will enable a circle around the bard, if he's holding effects!")),


    MinerEnabled("Miner",true,createComment("Enables the class")),
    MinerInvisibleEnabled("Miner",true,createComment("Enables the invisible effect after reaching a certain y limit.")),
    MinerInvisibleYLevel("Miner",45,createComment("Sets the Y limit, reaching this value will sets the miner invisible")),
    MaxMinerInFaction("Miner",-1,createComment("Max miner class users per faction. Set to -1 to disable the limit.")),

    RogueEnabled("Rogue",true,createComment("Enables the class")),
    BackstabEnabled("Rogue",true,createComment("Enables the backstab.")),
    BackstabDamageAmplifier("Rogue",100.0,createComment("Sets the damage amplifier.(% value)")),
    MaxRogueInFaction("Rogue",-1,createComment("Max rogue class users per faction. Set to -1 to disable the limit."));


    public String section;
    public Object value;
    public String[] comments;

    ClassConfig(Object value) {
        this.value = value;
    }

    ClassConfig(Object value, String[] comments) {
        this.value = value;
        this.comments = comments;
    }

    ClassConfig(String section, Object value) {
        this.value = value;
        this.section = section;
    }

    ClassConfig(String section, Object value, String[] comments) {
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
        return ConfigManager.getClassConfig().getList(this.getPath());
    }

    public List<String> asStrList() {
        return ConfigManager.getClassConfig().getStringList(this.getPath());
    }

    public List<String> asChatColorList() {
        List<String> cucc = new ArrayList<>();
        for (String s : asStrList()) {
            cucc.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return cucc;
    }

    public int asInt() {
        return ConfigManager.getClassConfig().getInt(this.getPath());
    }

    public double asDouble() {
        return ConfigManager.getClassConfig().getDouble(this.getPath());
    }

    public String asStr() {
        return ChatColor.translateAlternateColorCodes('&', this.value.toString());
    }

    public boolean asBoolean() {
        return ConfigManager.getClassConfig().getBoolean(this.getPath());
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
        SimpleConfig config = ConfigManager.getClassConfig();

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
        SimpleConfig config = ConfigManager.getClassConfig();

        if (this.section == null)
            config.set(this.toString(), this.value);
        else
            config.set(section + "." + this, this.value);

        config.saveConfig();
    }
}
