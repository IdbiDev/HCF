package me.idbi.hcf.events;


import me.idbi.hcf.CustomFiles.ConfigLibrary;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class onPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (Main.debug)
            System.out.println("Respawning player.. Loading it ");
        playertools.LoadPlayer(e.getPlayer());
        String str = ConfigLibrary.Spawn_location.getValue();
        Location spawn = new Location(
                Bukkit.getWorld(ConfigLibrary.World_name.getValue()),
                Integer.parseInt(str.split(" ")[0]),
                Integer.parseInt(str.split(" ")[1]),
                Integer.parseInt(str.split(" ")[2]),
                Integer.parseInt(str.split(" ")[3]),
                Integer.parseInt(str.split(" ")[4])
        );
        e.getPlayer().teleport(spawn);
        e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setFallDistance(0);

    }
}
