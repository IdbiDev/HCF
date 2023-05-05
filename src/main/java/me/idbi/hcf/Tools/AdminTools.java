package me.idbi.hcf.Tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class AdminTools {
    public static class InvisibleManager implements Listener {

        public static List<UUID> invisedAdmins;

        public static void hidePlayer(Player p) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target == p) continue;

                target.hidePlayer(p);
            }
            invisedAdmins.add(p.getUniqueId());
        }

        public static void showPlayer(Player admin) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(admin);
            }
            invisedAdmins.remove(admin.getUniqueId());
        }

        public static List<UUID> getInvisedAdmins() {
            return invisedAdmins;
        }
    }
}
