package me.idbi.hcf.CustomFiles.ConfigManagers;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.Comments.MessagesComments;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Configs.ConfigComments;
import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.CustomFiles.MessagesTool;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;

import java.io.File;

public class ConfigManager {

    private static SimpleConfigManager manager;

    private static SimpleConfig config;
    private static SimpleConfig messages;
    private static SimpleConfig enMessages;
    private static SimpleConfig guiMessages;
    private static SimpleConfig enGuiMessages;

    public static String[] configHeader = {"This is super simple", "And highly customizable", "new and fresh SimpleConfig !"}; // ToDo: Edited
    public static String[] messagesHeader = {"123", "123","123"}; // ToDo: Edited
    public static String[] guiHeader = {"GUIGUGIGUI", "GUIGUGIGUI","GUIGUGIGUI"}; // ToDo: Edited

    public Main plugin;
    private static ConfigManager instance;
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        this.instance = this;
    }

    public void setup() {
        String[] header = {"This is super simple", "And highly customizable", "new and fresh SimpleConfig!"};

        this.manager = new SimpleConfigManager(this.plugin);

        new MessagesComments();
        new ConfigComments();

        this.config = manager.getNewConfig("config.yml", header);
        this.setupConfig();

        this.messages = manager.getNewConfig("messages/messages_" + Config.default_language.asStr() + ".yml");
        enMessages = manager.getNewConfig("messages/messages_en.yml");
        this.setupMessages();

        this.guiMessages = manager.getNewConfig("gui_messages/guimessages_" + Config.default_language.asStr() + ".yml");
        enGuiMessages = manager.getNewConfig("gui_messages/guimessages_en.yml");
        this.setupGUI();

        File[] files = new File(plugin.getDataFolder() + "\\messages").listFiles();
        File[] guiFiles = new File(plugin.getDataFolder() + "\\gui_messages").listFiles();

        for (File file : files) {
            if(file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                Bukkit.getLogger().info("Language " + file.getName() + " successfully loaded!");
            }
        }

        for (File file : guiFiles) {
            if(file.getName().startsWith("guimessages_") && file.getName().endsWith(".yml")) {
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

    public static ConfigManager getConfigManager() {
        return instance;
    }

    public static SimpleConfig getMainMessages() {
        return messages;
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
}
