package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChange implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        hcfPlayer.createRollback(null, Rollback.RollbackLogType.WORLD_CHANGE);
        
        World from = e.getFrom();
        World to = p.getWorld();
        
        if(to.getName().equalsIgnoreCase(Config.EndName.asStr())) {
            p.teleport(Playertools.parseLoc(to, Config.EndSpawn.asStr()));
        }
        if(from.getName().equalsIgnoreCase(Config.EndName.asStr())) {
            if(to.getName().equalsIgnoreCase(Config.WorldName.asStr())) {
                p.teleport(Playertools.parseLoc(to, Config.EndOverworldLocation.asStr()));
            }
        }
    }
}
