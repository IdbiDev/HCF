package me.idbi.hcf.events;

import me.idbi.hcf.tools.HCF_Timer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class onConsumeItem implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if(e.getItem() == null) return;
        if(e.isCancelled()) return;
        if(e.getItem().getData().getData() == (byte) 0 && e.getItem().getType() == Material.GOLDEN_APPLE && !Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "adminDuty"))) {
            if(HCF_Timer.get_Golden_Apple_Time(e.getPlayer()) <= 0) {
                HCF_Timer.add_Golden_Apple_Time(e.getPlayer());
            } else e.setCancelled(true);
        } else if(e.getItem().getData().getData() == (byte) 1 && e.getItem().getType() == Material.GOLDEN_APPLE && !Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "adminDuty"))) {
            if(HCF_Timer.get_OP_Golden_Apple_Time(e.getPlayer()) <= 0) {
                HCF_Timer.add_OP_Golden_Apple_Time(e.getPlayer());
            } else e.setCancelled(true);
        }
    }
}
