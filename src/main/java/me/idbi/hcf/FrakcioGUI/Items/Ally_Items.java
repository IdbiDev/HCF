package me.idbi.hcf.FrakcioGUI.Items;

import me.idbi.hcf.tools.Objects.AllyFaction;
import me.idbi.hcf.tools.Objects.Faction;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Ally_Items {

    public static ItemStack ally() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 6);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Allies");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage allies!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack request() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eRequest for Ally");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to send a request to faction for alley!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack manageRequests() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Request");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage requests!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack sentRequests() {
        ItemStack is = new ItemStack(Material.EMERALD);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eSent Requests");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage sent requests!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack manageAllies() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Allies");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage Allies!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack manageAlly(AllyFaction ally) {
        ItemStack is = new ItemStack(Material.DIAMOND);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§e" + ally.getAllyFaction().name);
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack requestedAlly(Faction ally) {
        ItemStack is = new ItemStack(Material.DIAMOND);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§e" + ally.name);
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to abort the request!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permissions() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§ePermission Manager");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage permissions!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack abort() {
        ItemStack is = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cAbort");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to abort ally!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permission_friendlyFire() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oFriendly Fire");
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permission_useBlock() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oUse Block");
        im.addEnchant(Enchantment.DURABILITY, 2, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permission_viewItems() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oView Items");
        im.addEnchant(Enchantment.DURABILITY, 3, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack permission_breakBlocks() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§7Permission: §e§oBlock Break");
        im.addEnchant(Enchantment.DURABILITY, 4, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }
}
