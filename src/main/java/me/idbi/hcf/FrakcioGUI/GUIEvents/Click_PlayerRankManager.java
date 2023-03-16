package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_PlayerRankManager implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("'s Rank Manager")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        String playerName = ChatColor.stripColor(e.getView().getTitle().split("'")[0]);
        if (e.getCurrentItem().getType() == Material.PAPER) {
            if (e.getSlot() == 4) return;
            if (!e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {

                String rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);

//                Faction faction = playertools.getPlayerFaction((Player) e.getWhoClicked());

                Faction f = Playertools.getPlayerFaction((Player) e.getWhoClicked());
                assert f != null;
                FactionRankManager.Rank rank = f.FindRankByName(rankName);
                if (rank != null)
                    if (rank.isLeader())
                        return;

                if (offline.isOnline())
                    f.ApplyPlayerRank(offline.getPlayer(), rankName);
                else
                    f.ApplyPlayerRank(offline, rankName);

                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(Messages.gui_rank_change.language(((Player) e.getWhoClicked())).setRank(rankName).queue().replace("%player%", offline.getName()));
                GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.SUCCESS);
            } else {
                e.getWhoClicked().sendMessage(Messages.gui_rank_already_have.language(((Player) e.getWhoClicked())).queue());
                GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.ERROR);
            }
        }
    }
}
