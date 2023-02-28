package me.idbi.hcf.Koth.GUI;

import me.idbi.hcf.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class KOTHInvClickEvent implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§8KOTH Rewards")) return;
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        if (KOTHInventory.isGuiItem(e.getCurrentItem()))
            e.setCancelled(true);

        if (e.getCurrentItem().isSimilar(KOTHInventory.save())) {
            e.getWhoClicked().closeInventory();
        } else if (e.getCurrentItem().isSimilar(KOTHInventory.close())) {
            Main.kothRewardsGUI.add(e.getWhoClicked().getUniqueId());
            e.getWhoClicked().getOpenInventory().close();

            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.kothRewardsGUI.remove(e.getWhoClicked().getUniqueId());
                }
            }.runTaskLater(Main.getPlugin(Main.class), 10L);
        }
    }
}
