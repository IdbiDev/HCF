package me.idbi.hcf.FactionGUI.KickConfirm;

import me.idbi.hcf.FactionGUI.GUISound;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Menus.MemberManageInventory;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
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

    public static Inventory inv(String name) {
        Inventory inv = Bukkit.createInventory(null, 3 * 9, "§8Delete " + name + " Rank");

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

    @EventHandler
    public void onlick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Rank") && !e.getView().getTitle().startsWith("§8Delete ")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        if (e.getCurrentItem().isSimilar(confirm())) {
            String name = e.getView().getTitle().split(" ")[1];

            Faction faction = Playertools.getPlayerFaction((Player) e.getWhoClicked());

            FactionRankManager.delete(faction, name);
            e.getWhoClicked().closeInventory();
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.SUCCESS);
            return;
        }

        if (e.getCurrentItem().isSimilar(cancel())) {
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            GUISound.playSound((Player) e.getWhoClicked(), GUISound.HCFSounds.BACK);
        }
    }
}
