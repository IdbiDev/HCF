package me.idbi.hcf.Koth;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class AutoKoth {
    @Getter private static BukkitTask task;

    public void startAutoKoth() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (Config.AutoKoTHEnabled.asBoolean()) {
                    if (isStartKoth() && Koth.GLOBAL_AREA == null && (Bukkit.getOnlinePlayers().size() > Config.KoTHMinOnline.asInt())) {
                        Koth.StartRandomKoth();
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 20L, 20 * 60);
    }


}
