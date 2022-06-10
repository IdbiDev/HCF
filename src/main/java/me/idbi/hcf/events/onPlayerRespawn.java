package me.idbi.hcf.events;


import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class onPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (Main.debug)
            System.out.println("Respawning player.. Loading it ");
        playertools.LoadPlayer(e.getPlayer());

    }
}
