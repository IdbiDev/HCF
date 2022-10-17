package me.idbi.hcf.events;

import me.idbi.hcf.tools.Banhandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;


public class onPlayerPreJoin implements Listener {

    @EventHandler
    public void onPlayerPreJoin(AsyncPlayerPreLoginEvent e) {
        if (Banhandler.isPlayerBannedFromHCF(e.getUniqueId())) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§4HCF - Deathban\nHátralévő idő:§f\n" + Banhandler.getBanRemainingTime(e.getUniqueId()) + " §4perc");
        }
    }
}
