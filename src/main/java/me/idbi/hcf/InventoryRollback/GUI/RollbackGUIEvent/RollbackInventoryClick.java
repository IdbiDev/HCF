package me.idbi.hcf.InventoryRollback.GUI.RollbackGUIEvent;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.GUI.PlayerRollbackListInventory;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RollbackInventoryClick implements Listener {

    @EventHandler
    public void onRollback(InventoryClickEvent e) {
        if(e.getView().getTitle().contains("'s rollback #")) {
            String title = e.getView().getTitle();
            System.out.println("Elvileg ID: " + title.substring(title.indexOf("#")));
            if (!title.substring(title.indexOf("'")).matches("^[0-9]+$")) {
                return;
            }

            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(!e.getCurrentItem().hasItemMeta()) return;

            Player owner = (Player) e.getWhoClicked();
            OfflinePlayer player = Bukkit.getOfflinePlayer(title.substring(0, title.indexOf("'")));
            if (player.hasPlayedBefore()) {
                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player.getUniqueId());
                int id = Integer.parseInt(title.substring(title.indexOf("#")));

                Rollback currentRollback = hcfPlayer.getRollback(id);
                if (e.getClick() == ClickType.DROP) {
                    if (player.isOnline()) {
                        if (currentRollback.rollback()) {
                            e.getWhoClicked().sendMessage("Visszaadva!");
                        } else owner.sendMessage(Messages.not_found_player.language(owner).queue());
                    } else owner.sendMessage(Messages.not_found_player.language(owner).queue());
                } else {
                    if(player.isOnline()) {
                        int clickedId = e.getCurrentItem().getEnchantmentLevel(Enchantment.DURABILITY);
                        owner.openInventory(PlayerRollbackListInventory.inv(owner, player.getPlayer(), hcfPlayer.getRollback(clickedId)));
                    } else
                        owner.sendMessage(Messages.not_found_player.language(owner).queue());
                }
            } else
                e.getWhoClicked().sendMessage(Messages.not_found_player.language(owner).queue());
        }
    }
}
