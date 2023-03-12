package me.idbi.hcf.Reclaim;

import me.idbi.hcf.CustomFiles.ReclaimFile;
import org.bukkit.entity.Player;

import java.util.List;

public class ReclaimConfig {
    
    public static int getLives(Player p) {
        String rankName = getRankName(p);
        return ReclaimFile.get().getInt("Reclaims." + rankName + ".lives");
    }

    public static List<String> getCommands(Player p) {
        String rankName = getRankName(p);
        return ReclaimFile.get().getStringList("Reclaims." + rankName + ".commands");
    }
    
    public static String getRankName(Player p) {
        String permission = null;
        if(ReclaimFile.get().getConfigurationSection("Reclaims") == null) return null;
        for (String reclaims : ReclaimFile.get().getConfigurationSection("Reclaims").getKeys(false)) {
            if(p.hasPermission("factions.reclaim." + reclaims.toLowerCase())) {
                permission = reclaims;
            }
        }

        return permission;
    }

    public static void claimed(Player p) {
        List<String> list = ReclaimFile.get().getStringList("ClaimedReclaims");
        if(!list.contains(p.getUniqueId().toString())) {
            list.add(p.getUniqueId().toString());
            ReclaimFile.get().set("ClaimedReclaims", list);
            ReclaimFile.save();
        }
    }

    public static boolean isClaimed(Player p) {
        List<String> list = ReclaimFile.get().getStringList("ClaimedReclaims");
        return list.contains(p.getUniqueId().toString());
    }

    public static void resetClaim(Player p) {
        List<String> list = ReclaimFile.get().getStringList("ClaimedReclaims");
        if(list.contains(p.getUniqueId().toString())) {
            list.remove(p.getUniqueId().toString());
            ReclaimFile.get().set("ClaimedReclaims", list);
            ReclaimFile.save();
        }
    }
}
