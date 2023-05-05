package me.idbi.hcf.api;

import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HCFApi {

    public static HCFRules getRules() {
        return HCFRules.getRules();
    }

    /**
     * This will return HCFPlayer if ever joined to server, otherwise null
     * @param player
     * @return HCFPlayer or null
     */
    public static HCFPlayer getHCFPlayer(Player player) {
        return HCFPlayer.getPlayer(player);
    }

    /**
     * This will return HCFPlayer if ever joined to server, otherwise null
     * @param uuid
     * @return HCFPlayer or null
     */
    public static HCFPlayer getHCFPlayer(UUID uuid) {
        return HCFPlayer.getPlayer(uuid);
    }

    /**
     * This will return HCFPlayer if ever joined to server, otherwise null
     * @param name
     * @return HCFPlayer or null
     */
    public static HCFPlayer getHCFPlayer(String name) {
        return HCFPlayer.getPlayer(name);
    }
}
