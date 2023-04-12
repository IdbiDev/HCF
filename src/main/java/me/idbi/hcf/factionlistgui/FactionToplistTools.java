package me.idbi.hcf.factionlistgui;

import me.idbi.hcf.ClickableMessages.ClickableFactions;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FactionToplistTools {

    public static ArrayList<ItemStack> getPageFactions(Player p, List<Faction> factions, int page) {
        ArrayList<ItemStack> is = new ArrayList<>();

        if (page == 1) {
            List<Faction> newFactions = factions.subList(0, Math.min(factions.size(), 28));
            int count = 1;
            for (Faction pageFaction : newFactions) {
                is.add(FactionToplistItems.getFaction(p, pageFaction, count));
                count++;
            }
        }
        if (page * 28 <= factions.size()) {
            List<Faction> pageFactions = factions.subList(page * 28, Math.min(page * 28 + 28, factions.size()));
            int count = page * 28;
            for (Faction pageFaction : pageFactions) {
                is.add(FactionToplistItems.getFaction(p, pageFaction, count));
                count++;
            }
        }

        return is;
    }

    public static boolean hasNext(List<Faction> factions, int page) {
        boolean hasNext = false;
        if (factions.size() > 28 && factions.size() > (page * 28 + 28))
            hasNext = true;

        return hasNext;
    }

    public static boolean hasPrevious(List<Faction> factions, int page) {
        if(page <= 1)
            return false;
        return page * 28 <= factions.size();
    }
}
