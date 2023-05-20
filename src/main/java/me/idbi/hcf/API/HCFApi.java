package me.idbi.hcf.API;

import me.idbi.hcf.HCFRules;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HCFApi {
    private static final HCFCustomTimerApi c_api = new HCFCustomTimerApi();

    public static HCFRules getRules() {
        return HCFRules.getRules();
    }
    public static HCFServer getServer() {
        return HCFServer.getServer();
    }
    public static HashMap<World.Environment, HCFMap> getMaps() {
        return HCFServer.getServer().getMaps();
    }

    /**
     * This will return HCFPlayer if ever joined to server, otherwise null
     * @param player Player
     * @return HCFPlayer or null
     */
    public static HCFPlayer getHCFPlayer(Player player) {
        return HCFPlayer.getPlayer(player);
    }

    /**
     * This will return HCFPlayer if ever joined to server, otherwise null
     * @param uuid Player uuid
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
