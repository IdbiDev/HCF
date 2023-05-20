package me.idbi.hcf.Tools;

import me.idbi.hcf.Tools.Objects.HCFPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminTools {
    public static class InvisibleManager implements Listener {

        public static ArrayList<UUID> invisedAdmins;

        public static void hidePlayer(Player p) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (target == p) continue;
                if(HCFPermissions.vanish_bypass.check(target)) continue;

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

        public static ArrayList<UUID> getInvisedAdmins() {
            return invisedAdmins;
        }
    }
}
