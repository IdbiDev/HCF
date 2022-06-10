package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DiscordFile {
    public static Main m = Main.getPlugin(Main.class);
    private static File cfg;
    private static FileConfiguration file;

    public static void setup() {
        cfg = new File(m.getDataFolder(), "\\discord.yml"); //
        if (!cfg.exists()) {
            m.saveResource("discord.yml", false);
            try {
                cfg.createNewFile();
            } catch (IOException e) {
                // owww
                e.printStackTrace();
            }
        }
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public static FileConfiguration getDiscord() {
        return file;
    }

    public static void saveDiscord() {
        //File files = new File(m.getDataFolder(), "\\lang\\messages_en.yml");
        try {
            file.options().copyDefaults(true);
            file.save(cfg);
        } catch (IOException e) {
            System.out.println("Can't save language file");
        }
    }

    public static void reloadDiscord() {
        file = YamlConfiguration.loadConfiguration(cfg);
    }
}