package me.idbi.hcf.Events;

import me.idbi.hcf.Tools.HCF_Timer;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class onConsumeItem implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        if (e.isCancelled()) return;
        HCFPlayer player = HCFPlayer.getPlayer(e.getPlayer());
        if (e.getItem().getData().getData() == (byte) 0
                && e.getItem().getType() == Material.GOLDEN_APPLE
                && !player.isInDuty()) {

            if (HCF_Timer.get_Golden_Apple_Time(e.getPlayer()) != 0) {
                HCF_Timer.add_Golden_Apple_Time(e.getPlayer());
            } else e.setCancelled(true);
        } else if (e.getItem().getData().getData() == (byte) 1
                && e.getItem().getType() == Material.GOLDEN_APPLE
                && !player.isInDuty()) {

            if (HCF_Timer.get_OP_Golden_Apple_Time(e.getPlayer()) != 0) {
                HCF_Timer.add_OP_Golden_Apple_Time(e.getPlayer());
            } else e.setCancelled(true);
        }
    }
}
