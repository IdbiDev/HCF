package me.idbi.hcf.FactionGUI.Menus;

import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.SQL_Connection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class PlayerRankManagerInventory {
    public static Connection con = Main.getConnection();

    // --------------------- ONLINE PLAYER ---------------------------- //
    public static Inventory inv(Player owner, Player p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, p.getName() + "'s Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }


        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        for (Map.Entry<Integer, FactionRankManager.Rank> rank : Playertools.sortByPriority(Playertools.getPlayerFaction(p)).entrySet()) {
            if (hcfPlayer.getRank().getName().equalsIgnoreCase(rank.getValue().getName())) { // what is this gondolkodom rajta én isxyd?
                inv.addItem(rank(rank.getValue().getName(), true));
                continue;
            }
            inv.addItem(rank(rank.getValue().getName(), false));
        }

        inv.setItem(49, GUI_Items.back(owner));
        inv.setItem(4, activeRank(p));

        return inv;
    }

    // --------------------- OFFLINE PLAYER ---------------------------- //

    public static Inventory inv(Player owner, OfflinePlayer p) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, p.getName() + "'s Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if (i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }

        Faction faction = Playertools.getPlayerFaction(p);

        HashMap<String, Object> map = SQL_Connection.dbPoll(con, "SELECT * FROM members WHERE uuid='?'", p.getUniqueId().toString());

        for (Map.Entry<Integer, FactionRankManager.Rank> rank : Playertools.sortByPriority(Playertools.getPlayerFaction(p)).entrySet()) {
            if (faction.getRankByName(map.get("rank").toString()).getName().equalsIgnoreCase(rank.getValue().getName())) {
                inv.addItem(rank(rank.getValue().getName(), true));
                continue;
            }
            inv.addItem(rank(rank.getValue().getName(), false));
        }

        inv.setItem(49, GUI_Items.back(owner));
        inv.setItem(4, activeRank(p));

        return inv;
    }

    // ONLINE PLAYER
    public static ItemStack activeRank(Player target) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        Faction faction = Playertools.getPlayerFaction(target);

        assert faction != null;
        FactionRankManager.Rank rank = faction.getPlayerRank(target);

        im.setDisplayName("§7Active Rank: §a§o" + rank.getName());

        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        is.setItemMeta(im);
        return is;
    }

    // OFFLINE PLAYER

    public static ItemStack activeRank(OfflinePlayer target) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        Faction faction = Playertools.getPlayerFaction(target);

        HashMap<String, Object> map = SQL_Connection.dbPoll(con, "SELECT * FROM members WHERE uuid='?'", target.getUniqueId().toString());
        assert faction != null;
        FactionRankManager.Rank rank = faction.getRankByName(map.get("rank").toString());

        im.setDisplayName("§7Active Rank: §a§o" + rank.getName());

        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rank(String name, boolean haveRole) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName("§e" + name);

        if (haveRole) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        is.setItemMeta(im);
        return is;
    }
}
