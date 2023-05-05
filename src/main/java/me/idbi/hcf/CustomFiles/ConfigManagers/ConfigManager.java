package me.idbi.hcf.CustomFiles.ConfigManagers;

import me.idbi.hcf.CustomFiles.Configs.ClassConfig;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Configs.ConfigComments;
import me.idbi.hcf.CustomFiles.Configs.LimitConfig;
import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.CustomFiles.Messages.MessagesComments;
import me.idbi.hcf.CustomFiles.MessagesTool;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;

public class ConfigManager {

    public static String[] configHeader = {"HCF+ Config", "Created by", "Idbi & Koba"}; // ToDo: Edited
    public static String[] messagesHeader = {"123", "123", "123"}; // ToDo: Edited
    public static String[] guiHeader = {"GUIGUGIGUI", "GUIGUGIGUI", "GUIGUGIGUI"}; // ToDo: Edited
    private static SimpleConfigManager manager;
    private static SimpleConfig config;
    private static SimpleConfig limitConfig;
    private static SimpleConfig messages;
    public static HashMap<String, SimpleConfig> allMessages;
    public static HashMap<String, SimpleConfig> allGuiMessages;
    private static SimpleConfig enMessages;
    private static SimpleConfig guiMessages;
    private static SimpleConfig enGuiMessages;
    private static SimpleConfig classes;
    private static ConfigManager instance;
    public Main plugin;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public static ConfigManager getConfigManager() {
        return instance;
    }

    public static SimpleConfig getMainMessages() {
        return messages;
    }
    public static SimpleConfig getClassConfig() {
        return classes;
    }
    public static SimpleConfig getLimitConfig() {
        return limitConfig;
    }

    public static SimpleConfig getEnglishMessages() {
        return enMessages;
    }

    public static SimpleConfig getGUIEnglishMessages() {
        return enGuiMessages;
    }

    public static SimpleConfig getGUIMessages() {
        return guiMessages;
    }

    public static SimpleConfig getSimpleConfig() {
        return config;
    }

    public static SimpleConfigManager getSimpleConfigManager() {
        return manager;
    }

    public void setup() {
        allMessages = new HashMap<>();
        allGuiMessages = new HashMap<>();
        String[] header = {"This is super simple", "And highly customizable", "new and fresh SimpleConfig!"};

        manager = new SimpleConfigManager(this.plugin);

        new MessagesComments();
        new ConfigComments();

        config = manager.getNewConfig("config.yml", header);
        this.setupConfig();

        limitConfig = manager.getNewConfig("limits.yml");
        this.setupLimits();

        messages = manager.getNewConfig("messages/messages_" + Config.DefaultLanguage.asStr() + ".yml");
        enMessages = manager.getNewConfig("messages/messages_en.yml");
        this.setupMessages();

        guiMessages = manager.getNewConfig("gui_messages/guimessages_" + Config.DefaultLanguage.asStr() + ".yml");
        enGuiMessages = manager.getNewConfig("gui_messages/guimessages_en.yml");
        this.setupGUI();

        classes = manager.getNewConfig("classes.yml");
        this.setupClasses();

        File[] files = new File(plugin.getDataFolder() + "\\messages").listFiles();
        File[] guiFiles = new File(plugin.getDataFolder() + "\\gui_messages").listFiles();

        for (File file : files) {
            if (file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                //allMessages.put(file.getName().replace(".yml", ""), manager.getNewConfig("messages/" + file.getName()));
                Bukkit.getLogger().info("Language " + file.getName() + " successfully loaded!");
            }
        }

        for (File file : guiFiles) {
            if (file.getName().startsWith("guimessages_") && file.getName().endsWith(".yml")) {
                //allGuiMessages.put(file.getName().replace(".yml", ""), manager.getNewConfig("gui_messages/" + file.getName()));
                Bukkit.getLogger().info("GUI Language " + file.getName() + " successfully loaded!");
            }
        }

        MessagesTool.updateMessageFiles();
        MessagesTool.updateGuiMessageFiles();
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

    public void setupGUI() {
        for (GUIMessages msg : GUIMessages.values()) {
            msg.load();
        }
    }

    public void setupClasses() {
        for (ClassConfig msg : ClassConfig.values()) {
            msg.load();
        }
    }

    public void setupLimits() {
        for (LimitConfig msg : LimitConfig.values()) {
            msg.load();
        }
    }
}
