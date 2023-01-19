package me.idbi.hcf.HistoryGUI.History;

import me.idbi.hcf.HistoryGUI.GUITools;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class HistoryItems_2 {

    /*
        public ArrayList<HistoryEntrys.BalanceEntry> balanceHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.KickEntry> kickHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.JoinLeftEntry> joinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.FactionJoinLeftEntry> factionjoinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.InviteEntry> inviteHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.RankEntry> rankCreateHistory = new ArrayList<>();
     */

    public static ItemStack inviteHistory(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6☰ §eInvite History");

        if(f.inviteHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active status changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.InviteEntry inviteEntry : f.inviteHistory) {
            lore.add("§7" + counter + ". " + designInvite(inviteEntry));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.inviteHistory.size() / 5.0)));

        is.setItemMeta(im);
        return is;
    }

    public static ItemStack rankCreateHistory(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§2☰ §aRank History");

        if(f.rankCreateHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active rank changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.RankEntry rank : f.rankCreateHistory) {
            lore.add("§7" + counter + ". " + designRank(rank));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.rankCreateHistory.size() / 5.0)));

        is.setItemMeta(im);
        return is;
    }


    public static ItemStack fJoinLeft(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§2☰ §aFaction Join / Left History");

        if(f.factionjoinLeftHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active member changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.FactionJoinLeftEntry fJL : f.factionjoinLeftHistory) {
            lore.add("§7" + counter + ". " + designFJoinLeft(fJL));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.factionjoinLeftHistory.size() / 5.0)));

        is.setItemMeta(im);
        return is;
    }

    public static String designFJoinLeft(HistoryEntrys.FactionJoinLeftEntry entry) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date(entry.time));
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        return "§7[§a" + format.format(oneHourBack) + "§7] §2" + entry.player + "§7 has been " + entry.type + "§7.";
    }

    public static String designInvite(HistoryEntrys.InviteEntry entry) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date(entry.time));
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        String isInvited = entry.isInvited ? "invited" : "uninvited";
        return "§7[§6" + format.format(oneHourBack) + "§7] §e" + entry.player + "§7 " + isInvited + "§7 by §e" + entry.executor + "§7.";
    }

    public static String designRank(HistoryEntrys.RankEntry entry) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date(entry.time));
        cal.add(Calendar.HOUR, -1);
        Date oneHourBack = cal.getTime();

        String type = entry.type.equals("modify") ? "modified" : entry.type;
        return "§7[§a" + format.format(oneHourBack) + "§7] §a" + entry.rank + "§7 " + type + " by §2" + entry.player;
    }
}
