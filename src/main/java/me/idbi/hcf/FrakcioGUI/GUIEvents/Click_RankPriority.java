package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RP_Items;
import me.idbi.hcf.FrakcioGUI.Items.RPrio_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankPriorityInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Click_RankPriority implements Listener {

    private final static Main m = Main.getPlugin(Main.class);
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§8Rank Priority Manager")) return;

        e.setCancelled(true);
        if(e.getInventory().getItem(0).isSimilar(RPrio_Items.limeGlass())) {
            return;
        }

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if(!(e.getWhoClicked() instanceof Player p)) return;

        if (e.getCurrentItem().isSimilar(RP_Items.cancel())) {
            e.getWhoClicked().openInventory(MainInventory.mainInv(p));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if(e.getCurrentItem().isSimilar(RPrio_Items.rankManagerToggleButton())) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv(p));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if(e.getCurrentItem().getType() == Material.PAPER)
            if(!selectable(p, e.getCurrentItem()))
                return;

        if(e.getCurrentItem().getItemMeta().hasEnchants()) {
            RankPriorityInventory.editableInventoryRemoveEnchant(e.getInventory(), e.getCurrentItem(), e.getSlot());
            return;
        }

        if(e.getCurrentItem().isSimilar(RP_Items.save())) {
            saveInventory(p, e.getInventory());
            p.sendMessage(Messages.GUI_PRIORITY_SAVED.queue());
            GUI_Sound.playSound(p, "success");
            e.getWhoClicked().closeInventory();
            return;
        }

        if(hasSelectedOneItem(e.getInventory())) {
            // ToDo: Change
            ItemStack selected = getSelected(e.getInventory());
            if(selected == null) return;

            ItemStack currentItem = e.getCurrentItem();
            int selectedSlot = getSelectedSlot(e.getInventory());

            e.getInventory().setItem(selectedSlot, currentItem);
            e.getInventory().setItem(e.getSlot(), selected);
            p.updateInventory();

            RankPriorityInventory.editableInventoryRemoveEnchant(e.getInventory(), selected, e.getSlot());

            GUI_Sound.playSound(p, "success");
/*            Bukkit.broadcastMessage("Csere successfully!");*/
            changeGlass(true, e.getInventory());
            new BukkitRunnable() {
                @Override
                public void run() {
                    changeGlass(false, e.getInventory());
                }
            }.runTaskLater(m, 20L);
            return;
        }

        RankPriorityInventory.editableInventoryAddEnchant(e.getInventory(), e.getCurrentItem(), e.getSlot());
        GUI_Sound.playSound(p, "click");
    }

    public static ItemStack getSelected(Inventory inv) {
        for(ItemStack is : inv.getContents()) {
            if(is == null) continue;
            if(!is.hasItemMeta()) continue;
            if(is.getType() != Material.PAPER) continue;
            if(is.getItemMeta().hasEnchants()) return is;
        }
        return null;
    }

    public static void saveInventory(Player p, Inventory inv) {
        Main.Faction f = playertools.getPlayerFaction(p);
        for(ItemStack is : inv.getContents()) {
            if(is == null) return;
            if(!is.hasItemMeta()) return;
            if(is.getType() != Material.PAPER) continue;
            if(!selectable(p, is)) continue;

            String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
            assert f != null;
            Faction_Rank_Manager.Rank rank = f.FindRankByName(name);
            if(rank == null) continue;

            rank.priority = inv.first(is);

        }
        assert f != null;
        for (Faction_Rank_Manager.Rank r:
             f.ranks) {
            System.out.println(r.name + "   "+ r.priority);
        }
    }

    public static boolean selectable(Player p, ItemStack is) {
        String name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
        Main.Faction f = playertools.getPlayerFaction(p);

        assert f != null;
        return !(f.FindRankByName(name).isDefault || f.FindRankByName(name).isLeader);
    }

    public static int getSelectedSlot(Inventory inv) {
        for(ItemStack is : inv.getContents()) {
            if(is == null) continue;
            if(!is.hasItemMeta()) continue;
            if(is.getType() != Material.PAPER) continue;
            if(is.getItemMeta().hasEnchants()) return inv.first(is);
        }
        return -1;
    }

    public static boolean hasSelectedOneItem(Inventory inv) {
        for(ItemStack is : inv.getContents()) {
            if(is == null) continue;
            if(!is.hasItemMeta()) continue;
            if(is.getType() != Material.PAPER) continue;
            if(is.getItemMeta().hasEnchants()) return true;
        }
        return false;
    }

    public static void changeGlass(boolean lime, Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            boolean okaysDonatesDontes = i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44;

            if(inv.getItem(i) == null) continue;
            if(inv.getItem(i).getType() != Material.STAINED_GLASS_PANE) continue;

            if(lime) {
                if (i <= 8)
                    inv.setItem(i, RPrio_Items.limeGlass());
                else if (i >= 45)
                    inv.setItem(i, RPrio_Items.limeGlass());
                else if (okaysDonatesDontes)
                    inv.setItem(i, RPrio_Items.limeGlass());
            } else {
                if (i <= 8)
                    inv.setItem(i, GUI_Items.blackGlass());
                else if (i >= 45)
                    inv.setItem(i, GUI_Items.blackGlass());
                else if (okaysDonatesDontes)
                    inv.setItem(i, GUI_Items.blackGlass());
            }
        }
    }
}
