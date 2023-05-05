package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Menus.MemberListInventory;
import me.idbi.hcf.FactionGUI.Menus.MemberManageInventory;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
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
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
            return;
        }

        String playerName = ChatColor.stripColor(e.getView().getTitle().split("'")[0]);
        if (e.getCurrentItem().getType() == Material.PAPER) {
            if (e.getSlot() == 4) return;
            if (!e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {

                String rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                Player p = (Player) e.getWhoClicked();
                HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
                OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
                if(offline.getUniqueId() == e.getWhoClicked().getUniqueId()) return;

//                Faction faction = playertools.getPlayerFaction((Player) e.getWhoClicked());

                Faction f = Playertools.getPlayerFaction((Player) e.getWhoClicked());
                assert f != null;
                if(hcfPlayer.getRank().isLeader()) return;
                FactionRankManager.Rank rank = f.getRankByName(rankName);

                if (rank != null)
                    if (rank.isLeader())
                        return;

                if (offline.isOnline())
                    f.applyPlayerRank(offline.getPlayer(), rankName);
                else
                    f.applyPlayerRank(offline, rankName);


                p.openInventory(MemberManageInventory.manage(p, playerName));
                e.getWhoClicked().sendMessage(Messages.gui_rank_change.language(((Player) e.getWhoClicked())).setRank(rankName).queue().replace("%player%", offline.getName()));
                GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.SUCCESS);
            } else {
                e.getWhoClicked().sendMessage(Messages.gui_rank_already_have.language(((Player) e.getWhoClicked())).queue());
                GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.ERROR);
            }
        }
    }
}
