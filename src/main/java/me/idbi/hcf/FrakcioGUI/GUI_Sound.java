package me.idbi.hcf.FrakcioGUI;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GUI_Sound {

    public static void playSound(Player p, String state) {
        if (state.equalsIgnoreCase("error")) {
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

    public static void playSound(Player p, Sound state) {
        p.playSound(p.getLocation(), state, 1f, 1f);
    }

    public static void playSound(Player p, HCFSounds state) {
        if (state.equals(HCFSounds.ERROR)) {
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
        } else if (state.equals(HCFSounds.BACK)) {
            p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 1f, 1f);
        } else if (state.equals(HCFSounds.SUCCESS)) {
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
        } else if (state.equals(HCFSounds.CLICK)) {
            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
        } else if (state.equals(HCFSounds.LEVEL_UP)) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }
        System.out.println("PLAYING FUCKING SOUND FUCKING NORMIES: PLAYER: " + p.getName() + " SOUND:::::::::::" + state);
    }

    public enum HCFSounds {
        ERROR,
        BACK,
        SUCCESS,
        CLICK,
        LEVEL_UP
    }
}
