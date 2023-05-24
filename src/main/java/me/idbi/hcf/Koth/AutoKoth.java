package me.idbi.hcf.Koth;

import lombok.Getter;
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
    
    public static boolean isStartKoth() {
        List<String> list = Config.KoTHStartDate.asStrList();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String date = formatter.format(new Date());
        for (String s : list) {
            if(!s.matches("^[0-9:]{4,5}$")) continue;
            if(date.equalsIgnoreCase(s) || date.replaceFirst("0", "").equalsIgnoreCase(s)) {
                if(!s.contains(":")) continue;
                String min = date.split(":")[1];
                String sMin = s.split(":")[1];
                if(min.equalsIgnoreCase(sMin)) {
                    return true;
                }
            }
        }

        return false;
    }


}
