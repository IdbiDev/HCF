package me.idbi.hcf.FrakcioGUI.Items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RP_Items {

    public static ItemStack on() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Status: §aOn");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to §cdisable §7permission!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack off() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Status: §cOff");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to §aenable §7permission!"
        ));
        is.setItemMeta(im);
        return is;
    }

    /* ALL("-1"),      // Include faction manager
    BASIC("0"),     // Basic role
    INVITE("1"),    // Can or not invite other players
    SETHOME("2"),   // Can modify the faction home
    WITHDRAW("3"),  // Can withdraw from the faction balance
    KICK("4");    // Can rename the faction */

    public static ItemStack all() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oALL");
        im.setLore(Arrays.asList(
                "§5",
                "§7Description:",
                "§7- Include Faction Manager"
        ));
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        //is.setDurability((short) 1);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack basic() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oBasic");
        //is.setDurability((short) 2);
        im.addEnchant(Enchantment.DURABILITY, 2, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invite() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oInvite");
        im.addEnchant(Enchantment.DURABILITY, 3, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        //is.setDurability((short) 3);
        im.setLore(Arrays.asList(
                "§5",
                "§7Description:",
                "§7- Can invite players to faction."
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack sethome() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oSethome");
        //is.setDurability((short) 4);
        im.addEnchant(Enchantment.DURABILITY, 4, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.setLore(Arrays.asList(
                "§5",
                "§7Description:",
                "§7- Can edit the faction's home."
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack withdraw() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oWithdraw");
        im.addEnchant(Enchantment.DURABILITY, 5, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        //is.setDurability((short) 5);
        im.setLore(Arrays.asList(
                "§5",
                "§7Description:",
                "§7- Can withdraw money to the faction's balance."
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack kick() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oKick");
        im.addEnchant(Enchantment.DURABILITY, 6, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        //is.setDurability((short) 6);
        im.setLore(Arrays.asList(
                "§5",
                "§7Description:",
                "§7- Can kick player from the faction."
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack save() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§aSave All Changes");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack cancel() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cDiscard All Changes");
        is.setItemMeta(im);
        return is;
    }
}
