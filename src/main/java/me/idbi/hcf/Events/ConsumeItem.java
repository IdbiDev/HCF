package me.idbi.hcf.Events;

import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ConsumeItem implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem() == null) return;
        if (e.isCancelled()) return;
        HCFPlayer player = HCFPlayer.getPlayer(e.getPlayer());
        if (e.getItem().getData().getData() == (byte) 0
                && e.getItem().getType() == Material.GOLDEN_APPLE
                && !player.isInDuty()) {

            if (!Timers.GOLDEN_APPLE.has(player)) {
                Timers.GOLDEN_APPLE.add(player);
            } else e.setCancelled(true);
        } else if (e.getItem().getData().getData() == (byte) 1
                && e.getItem().getType() == Material.GOLDEN_APPLE
                && !player.isInDuty()) {

            if (!Timers.ENCHANTED_GOLDEN_APPLE.has(player)) {
                Timers.ENCHANTED_GOLDEN_APPLE.add(player);
            } else
                e.setCancelled(true);
        }
    }
}
