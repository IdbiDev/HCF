package me.idbi.hcf.HistoryGUI.History;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.Main;
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

    public static Inventory inv(Main.Faction f, int balancePage, int kickPage, int joinLeftPage, int fJoinLeftPage, int invitePage, int rankCreatePage) {
        Inventory inv = Bukkit.createInventory(null, 5*9, "§8" + f.name + "'s histories");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        if(balancePage < 1)
            balancePage = 1;
        if(balancePage > ((int) Math.ceil(f.balanceHistory.size() / 10.0)))
            balancePage = 1;

        inv.setItem(11, HistoryItems_1.balanceHistory(f, balancePage));
        inv.setItem(13, HistoryItems_1.kickHistory(f, kickPage));
        inv.setItem(15, HistoryItems_1.joinLeftHistory(f, joinLeftPage));
        inv.setItem(29, HistoryItems_2.fJoinLeft(f, fJoinLeftPage));
        inv.setItem(31, HistoryItems_2.inviteHistory(f, invitePage));
        inv.setItem(33, HistoryItems_2.rankCreateHistory(f, rankCreatePage));
        return inv;
    }
}
