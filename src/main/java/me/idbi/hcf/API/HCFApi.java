package me.idbi.hcf.API;

import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.ReclaimFile;
import me.idbi.hcf.HCFMap;
import me.idbi.hcf.HCFRules;
import me.idbi.hcf.HCFServer;
import me.idbi.hcf.Main;
import me.idbi.hcf.Reclaim.ReclaimConfig;
import me.idbi.hcf.Tools.Nametag.NameChanger;
import me.idbi.hcf.Tools.Objects.*;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static HCFCustomTimerApi getCustomTimerApi() {
        return c_api;
    }

    public static Claim getClaim(Location loc) {
        return Playertools.getUpperClaim(loc);
    }

    public static Claim getClaim(Player player) {
        return Playertools.getUpperClaim(player);
    }

    public static boolean isInWilderness(Player p) {
        return getClaim(p) == null;
    }

    public static boolean isInWilderness(Location loc) {
        return getClaim(loc) == null;
    }

    public static boolean isInWarzone(Player p) {
        return Playertools.isInWarzone(p);
    }

    public static boolean isInWarzone(Location loc) {
        return Playertools.isInWarzone(loc);
    }

    public static Faction getFaction(String name) {
        return Playertools.getFactionByName(name);
    }
    public static String setFakeName(Player p, String newName) {
        NameChanger.setFakeName(p.getUniqueId(), newName);
        return newName;
    }
    public static String getFakeName(Player p, String newName) {
        return NameChanger.getFakeName(p.getUniqueId());
    }

    public static Faction getFaction(OfflinePlayer player) {
        return Playertools.getPlayerFaction(player);
    }

    public static HashMap<Integer, Faction> getFactions() {
        return Main.factionCache;
    }

    public static HashMap<UUID, HCFPlayer> getHCFPlayers() {
        return Main.playerCache;
    }

    public static boolean inFaction(Player p) {
        return HCFPlayer.getPlayer(p).inFaction();
    }

    public static void resetReclaim(Player p) {
        ReclaimConfig.resetClaim(p);
    }

    public static void resetReclaims() {
        ReclaimFile.get().set("ClaimedReclaims", null);
        ReclaimFile.save();
    }

    /**
     *
     * @param claim
     * @return the players inside the claim
     */
    public static List<Player> getPlayersInClaim(Claim claim) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(claim.in(player.getLocation())) {
                players.add(player);
            }
        }
        return players;
    }
}
