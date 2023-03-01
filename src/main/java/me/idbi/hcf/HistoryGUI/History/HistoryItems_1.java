package me.idbi.hcf.HistoryGUI.History;

import me.idbi.hcf.HistoryGUI.GUITools;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryItems_1 {

    public static ItemStack joinLeftHistory(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6☰ §eJoin / Left History");

        if (f.joinLeftHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active status changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.JoinLeftEntry joinLeftEntry : f.joinLeftHistory) {
            lore.add("§7" + counter + ". " + designJoinLeft(joinLeftEntry));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.joinLeftHistory.size() / 5.0)));

        is.setItemMeta(im);
        return is;
    }

    public static ItemStack balanceHistory(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6☰ §eBalance History");

        if (f.balanceHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active bank changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.BalanceEntry balance : f.balanceHistory) {
            lore.add("§7" + counter + ". " + designBalanceHistory(balance));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.balanceHistory.size() / 10.0)));

        is.setItemMeta(im);
        return is;
    }

    public static ItemStack kickHistory(Faction f, int page) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§2☰ §aKick History");

        if (f.kickHistory.isEmpty()) {
            im.setLore(Arrays.asList("§5", "§7- §cNo active member changes!"));
            is.setItemMeta(im);
            return is;
        }

        ArrayList<String> lore = new ArrayList<>();

        int counter = 1;
        for (HistoryEntrys.KickEntry kick : f.kickHistory) {
            lore.add("§7" + counter + ". " + designKickHistory(kick));
            counter++;
        }

        im.setLore(GUITools.setupPageLore(page, lore, (int) Math.ceil(f.kickHistory.size() / 10.0)));

        is.setItemMeta(im);
        return is;
    }

    public static String designBalanceHistory(HistoryEntrys.BalanceEntry entry) {
        TimeZone timezone = TimeZone.getTimeZone("Europe/Budapest");
        Date date = new Date(entry.time);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(timezone);

        if (entry.amount < 0) {
            return "§7[§6" + format.format(date) + "§7] §e" + entry.player + "§7: [§6-$" + Math.abs(entry.amount) + "§7]";
        } else {
            return "§7[§6" + format.format(date) + "§7] §e" + entry.player + "§7: [§6+$" + entry.amount + "§7]";
        }
    }

    public static String designKickHistory(HistoryEntrys.KickEntry entry) {
        TimeZone timezone = TimeZone.getTimeZone("Europe/Budapest");
        Date date = new Date(entry.time);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(timezone);

        return "§7[§a" + format.format(date) + "§7] §2" + entry.player + "§7 kicked by §a" + entry.executor + "§7. " + "Reason: §a\"" + entry.reason + "\"";
    }

    public static String designJoinLeft(HistoryEntrys.JoinLeftEntry entry) {
        TimeZone timezone = TimeZone.getTimeZone("Europe/Budapest");
        Date date = new Date(entry.time);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(timezone);

        String status = (entry.isJoined) ? "joined" : "leaved";
        return "§7[§6" + format.format(date) + "§7] §e" + entry.player + "§7 " + status + "§7.";
    }
}
