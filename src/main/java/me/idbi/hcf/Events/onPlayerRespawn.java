package me.idbi.hcf.Events;


import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static me.idbi.hcf.Tools.HCF_Timer.addPvPTimerCoolDownSpawn;

public class onPlayerRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (Main.debug)
            System.out.println("Respawning player.. Loading it ");
        Playertools.loadOnlinePlayer(e.getPlayer());
        String str = Config.SpawnLocation.asStr();
        Location spawn = new Location(
                Bukkit.getWorld(Config.WorldName.asStr()),
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
        // Bukkit.broadcastMessage("Shut up nigger");
        addPvPTimerCoolDownSpawn(e.getPlayer());
    }
}
