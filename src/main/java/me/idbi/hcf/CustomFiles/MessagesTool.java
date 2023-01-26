package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.CustomFiles.ConfigManagers.ConfigManager;
import me.idbi.hcf.CustomFiles.ConfigManagers.SimpleConfig;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import org.bukkit.entity.Player;

import java.io.File;

public class MessagesTool {

    public static SimpleConfig getLanguageMessages(String language) {
        return ConfigManager.getSimpleConfigManager().getNewConfig("messages/messages_" + language + ".yml");
    }

    public static String getPlayerLanguage(Player p) {
        return (Main.currentLanguages.getOrDefault(p.getUniqueId(), Config.default_language.asStr()));
    }

    public static void setPlayerLanguage(Player p, String language) {
        if(Main.availableLanguages.contains(language)) {
            Main.currentLanguages.put(p.getUniqueId(), language);
        }
    }

    private static Main m = Main.getPlugin(Main.class);

    public static void updateMessageFiles() {
        File[] files = new File(m.getDataFolder() + "\\messages").listFiles();

        for (File file : files) {
            if(file.getName().equalsIgnoreCase("messages_en.yml")) continue;
            if(file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                String language = file.getName().replace("messages_", "");
                Main.availableLanguages.add(language.substring(0, language.lastIndexOf(".")));
                SimpleConfig config = ConfigManager.getSimpleConfigManager().getNewConfig("messages/" + file.getName());
                for (Messages value : Messages.values()) {
                    value.loadOthers(config);
                }
            }
        }
    }
}
