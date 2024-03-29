package me.idbi.hcf.FactionGUI.Items;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class IM_Items {

    public static ItemStack inviteManager(Player p) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_invite_manager.language(p).getName());
        im.setLore(GUIMessages.faction_invite_manager.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitePlayers(Player p) {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_invite_player.language(p).getName());
        im.setLore(GUIMessages.faction_invite_manager.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitedPlayers(Player p) {
        ItemStack is = new ItemStack(Material.EMERALD);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_invited_players.language(p).getName());
        im.setLore(GUIMessages.faction_invited_players.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack invitedPlayer(Player p, String name) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(name);
        im.setDisplayName(GUIMessages.faction_uninvite_player.language(p).setPlayerName(name).getName());
        im.setLore(GUIMessages.faction_invited_players.language(p).setPlayerName(name).getLore());
        is.setItemMeta(im);
        return is;
    }
}
