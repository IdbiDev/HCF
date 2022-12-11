package me.idbi.hcf.HistoryGUI.Events;

import me.idbi.hcf.HistoryGUI.GUITools;
import me.idbi.hcf.HistoryGUI.History.FactionHistoryInventory;
import me.idbi.hcf.HistoryGUI.History.HistoryItems_1;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HistoryEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().endsWith("'s histories")) return;
        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getType() != Material.PAPER) return;

        if(e.getClickedInventory() != e.getView().getTopInventory()) return;

        if(!(e.getWhoClicked() instanceof Player p)) return;
        Main.Faction f = playertools.getPlayerFaction(p);
        if(f == null) return;

        String displayName = e.getCurrentItem().getItemMeta().getDisplayName();

        ItemStack balance = e.getClickedInventory().getItem(11);
        ItemStack kick = e.getClickedInventory().getItem(13);
        ItemStack joinLeft = e.getClickedInventory().getItem(15);
        ItemStack fJoin = e.getClickedInventory().getItem(29);
        ItemStack invite = e.getClickedInventory().getItem(31);
        ItemStack rank = e.getClickedInventory().getItem(33);

        if(displayName.contains("§6☰ §eBalance History")) {
            if(e.getClick() == ClickType.RIGHT) {
                openBalance(p, e.getClickedInventory(), 1);
            } else if(e.getClick() == ClickType.LEFT) {
                openBalance(p, e.getClickedInventory(), -1);
            }
        }
//
        else if(e.getCurrentItem().isSimilar(HistoryItems_1.kickHistory(f, GUITools.getPage(kick)))) {
            if(e.getClick() == ClickType.RIGHT) {
                openKick(p, e.getClickedInventory(), 1);
            } else if(e.getClick() == ClickType.LEFT) {
                openKick(p, e.getClickedInventory(), -1);
            }
        }
    }

    public static int slot(ItemStack is) {
        int anyed = GUITools.getPage(is);
        return anyed;
    }

    public static void openBalance(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance) + page,
                slot(kick),
                slot(joinLeft),
                slot(fJoin),
                slot(invite),
                slot(rank)
        ));
    }

    public static void openKick(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick) - page,
                slot(joinLeft),
                slot(fJoin),
                slot(invite),
                slot(rank)
        ));
    }

    public static void openJoinLeft(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft) + page,
                slot(fJoin),
                slot(invite),
                slot(rank)
        ));
    }

    public static void openFJoinLeft(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft) ,
                slot(fJoin) + page,
                slot(invite),
                slot(rank)
        ));
    }

    public static void openInvite(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft) ,
                slot(fJoin),
                slot(invite) + page,
                slot(rank)
        ));
    }

    public static void openRank(Player p, Inventory inv, int page) {
        Main.Faction f = playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if(f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft) ,
                slot(fJoin),
                slot(invite),
                slot(rank) + page
        ));
    }
}
