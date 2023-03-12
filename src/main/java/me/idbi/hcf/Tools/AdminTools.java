package me.idbi.hcf.Tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class AdminTools {
    public static class InvisibleManager implements Listener {

        public static List<Player> invisedAdmins;

        public static void hidePlayer(Player p) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target == p) continue;

                target.hidePlayer(p);
            }
            invisedAdmins.add(p);
        }

        public static void showPlayer(Player admin) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(admin);
            }
            invisedAdmins.remove(admin);
        }

        public static List<Player> getInvisedAdmins() {
            return invisedAdmins;
        }
    }
}
