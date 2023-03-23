package me.idbi.hcf.Events;

import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerWorldChange implements Listener {

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(e.getPlayer());
        hcfPlayer.createRollback(e.getPlayer().getLastDamageCause().getCause(), Rollback.RollbackLogType.WORLD_CHANGE);
    }
}
