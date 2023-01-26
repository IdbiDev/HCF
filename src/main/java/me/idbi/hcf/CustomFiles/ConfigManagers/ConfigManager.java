package me.idbi.hcf.CustomFiles.ConfigManagers;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Comments.MessagesComments;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Configs.ConfigComments;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;

import java.io.File;

public class ConfigManager {

    private static SimpleConfigManager manager;

    private static SimpleConfig config;
    private static SimpleConfig messages;
    private static SimpleConfig enMessages;

    public Main plugin;
    private static ConfigManager instance;
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        this.instance = this;
    }

    public void setup() {
        String[] header = {"This is super simple", "And highly customizable", "new and fresh SimpleConfig !"};

        this.manager = new SimpleConfigManager(this.plugin);

        new MessagesComments();
        new ConfigComments();

        this.config = manager.getNewConfig("config.yml", header);
        this.setupConfig();

        this.messages = manager.getNewConfig("messages/messages_" + Config.default_language.asStr() + ".yml");
        enMessages = manager.getNewConfig("messages/messages_en.yml");
        this.setupMessages();

        File[] files = new File(plugin.getDataFolder() + "\\messages").listFiles();

        for (File file : files) {
            if(file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                Bukkit.getLogger().info("Language " + file.getName() + " successfully loaded!");
            }
        }
    }

    public void setupMessages() {
        for (Messages msg : Messages.values()) {
            msg.load();
        }
    }

    public void setupConfig() {
        for (Config config : Config.values()) {
            config.load();
        }
    }

    public static ConfigManager getConfigManager() {
        return instance;
    }

    public static SimpleConfig getMainMessages() {
        return messages;
    }

    public static SimpleConfig getEnglishMessages() {
        return enMessages;
    }

    public static SimpleConfig getSimpleConfig() {
        return config;
    }

    public static SimpleConfigManager getSimpleConfigManager() {
        return manager;
    }
}
