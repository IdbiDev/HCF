package me.idbi.hcf.HistoryGUI.History;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class FactionHistoryInventory {

        /*
        public ArrayList<HistoryEntrys.BalanceEntry> balanceHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.KickEntry> kickHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.JoinLeftEntry> joinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.FactionJoinLeftEntry> factionjoinLeftHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.InviteEntry> inviteHistory = new ArrayList<>();
        public ArrayList<HistoryEntrys.RankEntry> rankCreateHistory = new ArrayList<>();
     */

    public static Inventory inv(Faction f, int balancePage, int kickPage, int joinLeftPage, int fJoinLeftPage, int invitePage, int rankCreatePage) {
        Inventory inv = Bukkit.createInventory(null, 5 * 9, "§8" + f.getName() + "'s histories");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        subHistories(f);

        if (balancePage < 1)
            balancePage = 1;
        if (balancePage > ((int) Math.ceil(f.balanceHistory.size() / 10.0)))
            balancePage = 1;

        if (kickPage < 1)
            kickPage = 1;
        if (kickPage > ((int) Math.ceil(f.kickHistory.size() / 10.0)))
            kickPage = 1;

        if (joinLeftPage < 1)
            joinLeftPage = 1;
        if (joinLeftPage > ((int) Math.ceil(f.joinLeftHistory.size() / 10.0)))
            joinLeftPage = 1;

        if (fJoinLeftPage < 1)
            fJoinLeftPage = 1;
        if (fJoinLeftPage > ((int) Math.ceil(f.factionjoinLeftHistory.size() / 10.0)))
            fJoinLeftPage = 1;

        if (invitePage < 1)
            invitePage = 1;
        if (invitePage > ((int) Math.ceil(f.inviteHistory.size() / 10.0)))
            invitePage = 1;

        if (rankCreatePage < 1)
            rankCreatePage = 1;
        if (rankCreatePage > ((int) Math.ceil(f.rankCreateHistory.size() / 10.0)))
            rankCreatePage = 1;

        inv.setItem(11, HistoryItems_1.balanceHistory(f, balancePage));
        inv.setItem(13, HistoryItems_1.kickHistory(f, kickPage));
        inv.setItem(15, HistoryItems_1.joinLeftHistory(f, joinLeftPage));
        inv.setItem(29, HistoryItems_2.fJoinLeft(f, fJoinLeftPage));
        inv.setItem(31, HistoryItems_2.inviteHistory(f, invitePage));
        inv.setItem(33, HistoryItems_2.rankCreateHistory(f, rankCreatePage));
        return inv;
    }

    public static void subHistories(Faction f) {
        for (int x = 0; x <= f.rankCreateHistory.size() - 1; x++) {
            if (x >= 50) {
                f.rankCreateHistory.remove(f.rankCreateHistory.size() - 1);
            }
        }
        for (int x = 0; x <= f.kickHistory.size() - 1; x++) {
            if (x >= 50) {
                f.kickHistory.remove(f.kickHistory.size() - 1);
            }
        }
        for (int x = 0; x <= f.factionjoinLeftHistory.size() - 1; x++) {
            if (x >= 50) {
                f.factionjoinLeftHistory.remove(f.factionjoinLeftHistory.size() - 1);
            }
        }
        for (int x = 0; x <= f.balanceHistory.size() - 1; x++) {
            if (x >= 50) {
                f.balanceHistory.remove(f.balanceHistory.size() - 1);
            }
        }
        for (int x = 0; x <= f.joinLeftHistory.size() - 1; x++) {
            if (x >= 50) {
                f.joinLeftHistory.remove(f.joinLeftHistory.size() - 1);
            }
        }
        for (int x = 0; x <= f.inviteHistory.size() - 1; x++) {
            if (x >= 50) {
                f.inviteHistory.remove(f.inviteHistory.size() - 1);
            }
        }
    }
}
