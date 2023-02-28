package me.idbi.hcf.FrakcioGUI.Items;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RM_Items {

    public static ItemStack rank(Player p, String name) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.manage_rank.language(p).setRank(name).getName());
        im.setLore(GUIMessages.manage_rank.language(p).setRank(name).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack create(Player p) {
        ItemStack is = new ItemStack(Material.CHEST);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.create_rank.language(p).getName());
        im.setLore(GUIMessages.create_rank.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rename(Player p) {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.rename_rank.language(p).getName());
        im.setLore(GUIMessages.rename_rank.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permissionManager(Player p) {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.rank_permission_manager.language(p).getName());
        im.setLore(GUIMessages.rank_permission_manager.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack deleteRank(Player p) {
        ItemStack is = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.delete_rank.language(p).getName());
        im.setLore(GUIMessages.delete_rank.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }
}
