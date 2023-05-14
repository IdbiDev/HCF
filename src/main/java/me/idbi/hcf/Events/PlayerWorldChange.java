package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.HCFMap;
import me.idbi.hcf.HCFServer;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Tasks;
import me.idbi.hcf.Tools.WaypointPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerWorldChange implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        hcfPlayer.createRollback(null, Rollback.RollbackLogType.WORLD_CHANGE);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        World from = e.getFrom().getWorld();
        World to = e.getTo().getWorld();
        Player p = e.getPlayer();

        Playertools.refreshPosition(p);
        Tasks.executeLater(5, () -> {
            HCFPlayer.getPlayer(p).getWaypointPlayer().changeWorld();
        });
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) return;
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) return;
        if(from.equals(to)) return;

        if(to.getName().equalsIgnoreCase(Config.EndName.asStr())) {
            Tasks.executeLater(3, () -> p.teleport(HCFServer.getServer().getMap(World.Environment.THE_END).getWorldSpawn()));
        }

        if(from.getName().equalsIgnoreCase(Config.EndName.asStr())) {
            if (to.getName().equalsIgnoreCase(Config.WorldName.asStr())) {
                Tasks.executeLater(3, () -> p.teleport(Playertools.parseLoc(to, Config.EndOverworldLocation.asStr())));
            }
        }
    }
}
