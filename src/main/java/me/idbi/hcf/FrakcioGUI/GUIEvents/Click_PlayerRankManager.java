package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
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

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(MemberListInventory.members((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        String playerName = ChatColor.stripColor(e.getView().getTitle().split("'")[0]);
        if(e.getCurrentItem().getType() == Material.PAPER) {
            if(e.getSlot() == 4) return;
            if(!e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY)) {

                String rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);

//                Faction faction = playertools.getPlayerFaction((Player) e.getWhoClicked());

                Faction f = playertools.getPlayerFaction((Player) e.getWhoClicked());
                assert f != null;
                Faction_Rank_Manager.Rank rank = f.FindRankByName(rankName);
                if(rank != null)
                    if(rank.isLeader)
                        return;

                if(offline.isOnline())
                    f.ApplyPlayerRank(offline.getPlayer(), rankName);
                else
                    f.ApplyPlayerRank(offline, rankName);

                e.getWhoClicked().closeInventory();
                e.getWhoClicked().sendMessage(Messages.GUI_RANK_CHANGE.setRank(rankName).queue().replace("%player%", offline.getName()));
                GUI_Sound.playSound((Player) e.getWhoClicked(), "success");
            } else {
                e.getWhoClicked().sendMessage(Messages.GUI_RANK_ALREADY_HAVE.queue());
                GUI_Sound.playSound((Player) e.getWhoClicked(), "error");
            }
        }
    }
}
