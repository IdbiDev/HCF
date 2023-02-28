package me.idbi.hcf.FrakcioGUI.Items;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPrio_Items {

    public static ItemStack ranks(Player p, String rankName) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_ranks.language(p).setRank(rankName).getName());
        im.setLore(GUIMessages.faction_ranks.language(p).setRank(rankName).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack limeGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack priorityToggleButton(Player p) {
        ItemStack is = new ItemStack(Material.EMPTY_MAP);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.priority_toggle_button.language(p).getName());
        im.setLore(GUIMessages.priority_toggle_button.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rankManagerToggleButton(Player p) {
        ItemStack is = new ItemStack(Material.EMPTY_MAP);
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        im.setDisplayName(GUIMessages.rank_manager_toggle_button.language(p).getName());
        im.setLore(GUIMessages.rank_manager_toggle_button.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack selected(Player p, ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.setLore(GUIMessages.rank_manager_toggle_button.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }
}
