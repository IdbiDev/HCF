package me.idbi.hcf.FrakcioGUI.Menus;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.SQL_Connection;
import me.idbi.hcf.tools.playertools;
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
    public static Connection con = Main.getConnection("cmd.FactionCreate");
    // --------------------- ONLINE PLAYER ---------------------------- //
    public static Inventory inv(Player owner, Player p) {
        Inventory inv = Bukkit.createInventory(null, 6*9, p.getName() + "'s Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }

/*        List<String> namesGecim = new ArrayList<>();
        ArrayList<Faction_Rank_Manager.Rank> list = Objects.requireNonNull(playertools.getPlayerFaction(p)).ranks;

        for(Faction_Rank_Manager.Rank rank : list)
            namesGecim.add(rank.name);

        Collections.sort(namesGecim, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        Faction faction = playertools.getPlayerFaction(p);
        assert faction != null;
        for (String name : namesGecim) {
            String Name = ChatColor.stripColor(name);
            if(faction.player_ranks.get(p).name.equalsIgnoreCase(Name)) {
                inv.addItem(rank(Name, true));
                continue;
            }
            inv.addItem(rank(Name, false));
        }*/

        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        for(Map.Entry<Integer, Faction_Rank_Manager.Rank> rank : playertools.sortByPriority(playertools.getPlayerFaction(p)).entrySet()) {
            if(hcfPlayer.rank.name.equalsIgnoreCase(rank.getValue().name)) { // what is this gondolkodom rajta én isxyd?
                inv.addItem(rank(rank.getValue().name, true));
                continue;
            }
            inv.addItem(rank(rank.getValue().name, false));
        }

        inv.setItem(49, GUI_Items.back(owner));
        inv.setItem(4, activeRank(p));

        return inv;
    }

    // --------------------- OFFLINE PLAYER ---------------------------- //

    public static Inventory inv(Player owner, OfflinePlayer p) {
        Inventory inv = Bukkit.createInventory(null, 6*9, p.getName() + "'s Rank Manager");

        for (int i = 0; i < inv.getSize(); i++) {
            if(i <= 8)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i >= 45)
                inv.setItem(i, GUI_Items.blackGlass());
            else if(i == 9 || i == 17 || i == 18 || i == 26 || i == 27 || i == 35 || i == 36 || i == 44)
                inv.setItem(i, GUI_Items.blackGlass());

        }

/*        List<String> namesGecim = new ArrayList<>();
        ArrayList<Faction_Rank_Manager.Rank> list = Objects.requireNonNull(playertools.getPlayerFaction(p)).ranks;

        for(Faction_Rank_Manager.Rank rank : list)
            namesGecim.add(rank.name);

        Collections.sort(namesGecim, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        Faction faction = playertools.getPlayerFaction(p);
        HashMap<String,Object> map = SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?'",p.getUniqueId().toString());
        for (String name : namesGecim) {
            String Name = ChatColor.stripColor(name);
            assert faction != null;
            if(faction.FindRankByName(map.get("rank").toString()).name.equalsIgnoreCase(Name)) {
                inv.addItem(rank(Name, true));
                continue;
            }
            inv.addItem(rank(Name, false));
        }*/

        Faction faction = playertools.getPlayerFaction(p);

        HashMap<String,Object> map = SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?'",p.getUniqueId().toString());

        for(Map.Entry<Integer, Faction_Rank_Manager.Rank> rank : playertools.sortByPriority(playertools.getPlayerFaction(p)).entrySet()) {
            if(faction.FindRankByName(map.get("rank").toString()).name.equalsIgnoreCase(rank.getValue().name)) {
                inv.addItem(rank(rank.getValue().name, true));
                continue;
            }
            inv.addItem(rank(rank.getValue().name, false));
        }

        inv.setItem(49, GUI_Items.back(owner));
        inv.setItem(4, activeRank(p));

        return inv;
    }

    // ONLINE PLAYER
    public static ItemStack activeRank(Player target) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        Faction faction = playertools.getPlayerFaction(target);

        assert faction != null;
        Faction_Rank_Manager.Rank rank = faction.getPlayerRank(target);

        im.setDisplayName("§7Active Rank: §a§o" + rank.name);

        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        is.setItemMeta(im);
        return is;
    }

    // OFFLINE PLAYER

    public static ItemStack activeRank(OfflinePlayer target) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        Faction faction = playertools.getPlayerFaction(target);

        HashMap<String,Object> map = SQL_Connection.dbPoll(con,"SELECT * FROM members WHERE uuid='?'",target.getUniqueId().toString());
        assert faction != null;
        Faction_Rank_Manager.Rank rank = faction.FindRankByName(map.get("rank").toString());

        im.setDisplayName("§7Active Rank: §a§o" + rank.name);

        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rank(String name, boolean haveRole) {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName("§e" + name);

        if(haveRole) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        is.setItemMeta(im);
        return is;
    }
}
