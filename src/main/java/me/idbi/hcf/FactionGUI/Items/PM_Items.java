package me.idbi.hcf.FactionGUI.Items;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PM_Items {

    public static ItemStack rankManager(Player p) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_player_rank_manager.language(p).getName());
        im.setLore(GUIMessages.faction_player_rank_manager.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack kick(Player p) {
        ItemStack is = new ItemStack(Material.REDSTONE_ORE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_kick_player.language(p).getName());
        im.setLore(GUIMessages.faction_kick_player.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }
}
