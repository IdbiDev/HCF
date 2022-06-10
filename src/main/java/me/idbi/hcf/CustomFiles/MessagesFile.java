package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile {
    public static Main m = Main.getPlugin(Main.class);
    private static File cfg;
    private static FileConfiguration file;

    public static void setup() {
        cfg = new File(m.getDataFolder(), "\\lang"); //
        cfg.mkdirs();
        cfg = new File(m.getDataFolder() + "\\lang\\messages_en.yml");
        if (!cfg.exists()) {
            try {
                cfg.createNewFile();
            } catch (IOException e) {
                // owww
            }
        }
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public static FileConfiguration getMessages() {
        return file;
    }

    public static void saveMessages() {
        //File files = new File(m.getDataFolder(), "\\lang\\messages_en.yml");
        try {
            file.save(cfg);
        } catch (IOException e) {
            System.out.println("Can't save language file");
        }
    }

    public static void reloadMessages() {
        file = YamlConfiguration.loadConfiguration(cfg);
    }
}
