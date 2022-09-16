package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class IM_Items {

    public static ItemStack inviteManager() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eInvite Manager");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage the invites!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitePlayers() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eInvite Player");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to invite a player!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitedPlayers() {
        ItemStack is = new ItemStack(Material.EMERALD);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eInvited Players");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to show the invited players!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitedPlayers(Player p) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(p.getName());
        im.setDisplayName("§e" + p.getName());
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to §euninvite " + p.getName() + "§7!"
        ));
        is.setItemMeta(im);
        return is;
    }
}
