package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.io.File;

public class MessagesTool {

    private static final Main m = Main.getPlugin(Main.class);

    public static SimpleConfig getLanguageMessages(String language) {
        if(ConfigManager.allMessages.containsKey(language)) {
            return ConfigManager.allMessages.get(language);
        } else {
            return ConfigManager.getMainMessages();
        }
       // return ConfigManager.getSimpleConfigManager().getNewConfig("messages/messages_" + language + ".yml");
    }

    public static SimpleConfig getGUILanguageMessages(String language) {
        if(ConfigManager.allGuiMessages.containsKey(language)) {
            return ConfigManager.allGuiMessages.get(language);
        } else {
            return ConfigManager.getGUIMessages();
        }
        //return ConfigManager.getSimpleConfigManager().getNewConfig("gui_messages/messages_" + language + ".yml");
    }

    /*public static String getPlayerLanguage(Player p) {
        if(Main.currentLanguages.containsKey(p.getUniqueId())) {
            return Main.currentLanguages.get(p.getUniqueId());
        }
        return Config.DefaultLanguage.asStr();
    }
    public static String getPlayerLanguage(HCFPlayer hcfPlayer) {
        if(Main.currentLanguages.containsKey(hcfPlayer.getUUID())) {
            return Main.currentLanguages.get(hcfPlayer.getUUID());
        }
        return Config.DefaultLanguage.asStr();
    }*/

    /*public static void setPlayerLanguage(Player p, String language) {
        if (Main.availableLanguages.contains(language)) {
            Main.currentLanguages.put(p.getUniqueId(), language);
        }
    }*/

    public static void updateMessageFiles() {
        File[] files = new File(m.getDataFolder() + "\\messages").listFiles();

        for (File file : files) {
            //if (file.getName().equalsIgnoreCase("messages_en.yml")) continue;
            if (file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                String language = file.getName().replace("messages_", "");
                Main.availableLanguages.add(language.substring(0, language.lastIndexOf(".")));
                SimpleConfig config = ConfigManager.getSimpleConfigManager().getNewConfig("messages/" + file.getName());
                for (Messages value : Messages.values()) {
                    value.loadOthers(config);
                }

                ConfigManager.allMessages.put(language.replace(".yml", ""), config);
            }
        }
    }

    public static void updateGuiMessageFiles() {
        File[] files = new File(m.getDataFolder() + "\\gui_messages").listFiles();

        for (File file : files) {
            //if (file.getName().equalsIgnoreCase("guimessages_en.yml")) continue;
            if (file.getName().startsWith("guimessages_") && file.getName().endsWith(".yml")) {
                String language = file.getName().replace("guimessages_", "");
                Main.availableLanguages.add(language.substring(0, language.lastIndexOf(".")));
                SimpleConfig config = ConfigManager.getSimpleConfigManager().getNewConfig("gui_messages/" + file.getName());
                for (GUIMessages value : GUIMessages.values()) {
                    value.loadOthers(config);
                }
                ConfigManager.allGuiMessages.put(language.replace(".yml", ""), config);
            }
        }
    }
}
