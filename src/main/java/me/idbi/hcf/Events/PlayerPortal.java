package me.idbi.hcf.Events;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortal implements Listener {
    @EventHandler
    public void onPlayerPortalChange(PlayerPortalEvent e){
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(e.getPlayer());
        if(Timers.PVP_TIMER.has(hcfPlayer)) {
            e.setCancelled(true);
        }
    }
}
