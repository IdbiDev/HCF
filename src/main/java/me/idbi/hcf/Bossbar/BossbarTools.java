package me.idbi.hcf.Bossbar;

import org.bukkit.entity.Player;

public class BossbarTools {

    public static void set(Player p, String message) {
        //if (!Bossbar.bars.containsKey(p.getName())) {
            //Bossbar.bars.put(p.getName(), new Bossbar(p, ChatColor.translateAlternateColorCodes('&', message)));
        //}
    }

    public static void remove(Player p) {
        if (Bossbar.bars.containsKey(p.getName())) {
            ((Bossbar)Bossbar.bars.get(p.getName())).end();
            Bossbar.bars.remove(p.getName());
        }
    }
}
