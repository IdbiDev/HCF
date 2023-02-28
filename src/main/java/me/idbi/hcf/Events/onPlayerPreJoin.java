package me.idbi.hcf.Events;

import me.idbi.hcf.Tools.BanHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;


public class onPlayerPreJoin implements Listener {

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
        if (BanHandler.isPlayerBannedFromHCF(e.getUniqueId())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§4HCF - Deathban\nHátralévő idő:§f\n" + BanHandler.getBanRemainingTime(e.getUniqueId()) + " §4perc");
        }
    }
}
