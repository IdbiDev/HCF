package me.idbi.hcf.FrakcioGUI;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GUI_Sound {

    public static void playSound(Player p,String state) {
        if(state.equalsIgnoreCase("error")){
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
        } else if (state.equalsIgnoreCase("back")) {
            p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 1f, 1f);
        } else if (state.equalsIgnoreCase("success")) {
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        } else if (state.equalsIgnoreCase("click")) {
            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
        } else {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }
    }
}
