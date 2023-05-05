package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ReclaimFile {

    public static Main m = Main.getPlugin(Main.class);
    private static File cfg;
    private static FileConfiguration file;

    public static void setup() {
        cfg = new File(m.getDataFolder(), "reclaim.yml"); //
        if (!cfg.exists()) {
            m.saveResource("reclaim.yml", false);
           /* try {
                //cfg.createNewFile();
            } catch (IOException e) {
                // owww
                e.printStackTrace();
            }*/
        }
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public static FileConfiguration get() {
        return file;
    }

    public static void save() {
        //File files = new File(m.getDataFolder(), "\\lang\\messages_en.yml");
        try {
            file.options().copyDefaults(true);
            file.save(cfg);
        } catch (IOException e) {
            System.out.println("Can't save language file");
        }
    }

    public static void reload() {
        file = YamlConfiguration.loadConfiguration(cfg);
    }
}
