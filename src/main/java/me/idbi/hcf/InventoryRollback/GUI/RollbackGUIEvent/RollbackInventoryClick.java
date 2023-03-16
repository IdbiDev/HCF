package me.idbi.hcf.InventoryRollback.GUI.RollbackGUIEvent;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.GUI.PlayerRollbackListInventory;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        if (e.getView().getTitle().contains("'s rollback #")) {
            String title = e.getView().getTitle();
            if (!title.substring(title.indexOf("#") + 1).matches("^[0-9]+$")) {
                return;
            }

            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (!e.getCurrentItem().hasItemMeta()) return;

            Player owner = (Player) e.getWhoClicked();
            OfflinePlayer player = Bukkit.getOfflinePlayer(title.substring(0, title.indexOf("'")));
            if (player.hasPlayedBefore()) {
                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player.getPlayer());
                int id = Integer.parseInt(title.substring(title.indexOf("#") + 1));

                Rollback currentRollback = hcfPlayer.getRollback(id);
                if (e.getClick() == ClickType.DROP) {
                    if (e.getCurrentItem().getType() == Material.BLAZE_ROD || e.getCurrentItem().getType() == Material.WOOL) {
                        if (e.getCurrentItem().getType() == Material.WOOL) {
                            int clickedId = e.getCurrentItem().getEnchantmentLevel(Enchantment.DURABILITY);
                            if (id != clickedId) {
                                return;
                            }
                        }
                        if (player.isOnline()) {
                            if (currentRollback.rollback()) {
                                owner.sendMessage("Visszaadva!");
                                owner.openInventory(PlayerRollbackListInventory.inv(owner, player.getPlayer(), hcfPlayer.getRollback(id)));
                            } else owner.sendMessage(Messages.not_found_player.language(owner).queue());
                        } else owner.sendMessage(Messages.not_found_player.language(owner).queue());
                    }
                } else {
                    if (player.isOnline()) {
                        int clickedId = e.getCurrentItem().getEnchantmentLevel(Enchantment.DURABILITY);
                        owner.openInventory(PlayerRollbackListInventory.inv(owner, player.getPlayer(), hcfPlayer.getRollback(clickedId)));
                    } else
                        owner.sendMessage(Messages.not_found_player.language(owner).queue());
                }
            } else owner.sendMessage(Messages.not_found_player.language(owner).queue());
        }
    }
}

