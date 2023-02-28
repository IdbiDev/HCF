package me.idbi.hcf.HistoryGUI.Events;

import me.idbi.hcf.HistoryGUI.GUITools;
import me.idbi.hcf.HistoryGUI.History.FactionHistoryInventory;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HistoryEvent implements Listener {

    public static int slot(ItemStack is) {
        int anyed = GUITools.getPage(is);
        return anyed;
    }

    public static void openBalance(Player p, Inventory inv, int page) {
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

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
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick) + page,
                slot(joinLeft),
                slot(fJoin),
                slot(invite),
                slot(rank)
        ));
    }

    public static void openJoinLeft(Player p, Inventory inv, int page) {
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

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
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft),
                slot(fJoin) + page,
                slot(invite),
                slot(rank)
        ));
    }

    public static void openInvite(Player p, Inventory inv, int page) {
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft),
                slot(fJoin),
                slot(invite) + page,
                slot(rank)
        ));
    }

    public static void openRank(Player p, Inventory inv, int page) {
        Faction f = Playertools.getPlayerFaction(p);

        ItemStack balance = inv.getItem(11);
        ItemStack kick = inv.getItem(13);
        ItemStack joinLeft = inv.getItem(15);
        ItemStack fJoin = inv.getItem(29);
        ItemStack invite = inv.getItem(31);
        ItemStack rank = inv.getItem(33);

        if (f == null) return;

        p.openInventory(FactionHistoryInventory.inv(f,
                slot(balance),
                slot(kick),
                slot(joinLeft),
                slot(fJoin),
                slot(invite),
                slot(rank) + page
        ));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("'s histories")) return;
        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() != Material.PAPER) return;

        if (e.getClickedInventory() != e.getView().getTopInventory()) return;

        if (!(e.getWhoClicked() instanceof Player p)) return;
        Faction f = Playertools.getPlayerFaction(p);
        if (f == null) return;

        String displayName = e.getCurrentItem().getItemMeta().getDisplayName();

        if (displayName.contains("§6☰ §eBalance History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openBalance(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openBalance(p, e.getClickedInventory(), -1);
            }
        }
//
        else if (displayName.contains("§2☰ §aKick History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openKick(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openKick(p, e.getClickedInventory(), -1);
            }
        }
//
        else if (displayName.contains("§6☰ §eJoin / Left History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openJoinLeft(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openJoinLeft(p, e.getClickedInventory(), -1);
            }
        }
//
        else if (displayName.contains("§6☰ §eInvite History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openInvite(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openInvite(p, e.getClickedInventory(), -1);
            }
        }
//
        else if (displayName.contains("§2☰ §aRank History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openRank(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openRank(p, e.getClickedInventory(), -1);
            }
        }
//
        else if (displayName.contains("§2☰ §aFaction Join / Left History")) {
            if (e.getClick() == ClickType.RIGHT) {
                openFJoinLeft(p, e.getClickedInventory(), 1);
            } else if (e.getClick() == ClickType.LEFT) {
                openFJoinLeft(p, e.getClickedInventory(), -1);
            }
        }
    }
}
