package me.idbi.hcf.FrakcioGUI.KickConfirm;

import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DeleteRankConfirm implements Listener {

    @EventHandler
    public void onlick(InventoryClickEvent e) {
        if(!e.getView().getTitle().endsWith(" Rank") && !e.getView().getTitle().startsWith("§8Delete ")) return;

        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;

        if(e.getCurrentItem().isSimilar(confirm())) {
            String name = e.getView().getTitle().split(" ")[1];

            Main.Faction faction = playertools.getPlayerFaction((Player) e.getWhoClicked());

            Faction_Rank_Manager.DeleteRank(faction, name);
            e.getWhoClicked().closeInventory();
            Bukkit.broadcastMessage("Törölve");
            GUI_Sound.playSound((Player) e.getWhoClicked(), "success");
            return;
        }

        if(e.getCurrentItem().isSimilar(cancel())) {
            e.getWhoClicked().closeInventory();
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
        }
    }

    public static Inventory inv(String name) {
        Inventory inv = Bukkit.createInventory(null, 3*9, "§8Delete " + name + " Rank");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(11, confirm());
        inv.setItem(15, cancel());

        return inv;
    }

    public static ItemStack confirm() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§aConfirm");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack cancel() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cCancel");
        is.setItemMeta(im);
        return is;
    }
}