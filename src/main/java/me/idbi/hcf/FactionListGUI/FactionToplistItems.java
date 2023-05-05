package me.idbi.hcf.FactionListGUI;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.Tools.IItemBuilder;
import me.idbi.hcf.Tools.Objects.Faction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FactionToplistItems {

    public static ItemStack getFaction(Player p, Faction faction, int number) {
        ItemStack stats = GUI_Items.factionStats(p, faction);
        return IItemBuilder
                .create(stats)
                .setType(Material.PAPER)
                .setName(Messages.faction_top_list_gui_prefix.language(p).setNumber(number).queue() + stats.getItemMeta().getDisplayName())
                .finish();
    }

    public static ItemStack currentPage(Player p, int currentPage) {
        return IItemBuilder
                .create(Material.DOUBLE_PLANT)
                .setName(GUIMessages.faction_toplist_page.language(p).setPage(currentPage).getName())
                .setLore(GUIMessages.faction_toplist_page.language(p).setPage(currentPage).getLore())
                .finish();
    }

    public static ItemStack nextPage(Player p, int currentPage) {
        return IItemBuilder
                .create(Material.DOUBLE_PLANT)
                .setName(GUIMessages.faction_toplist_next.language(p).setPage(currentPage).getName())
                .setLore(GUIMessages.faction_toplist_next.language(p).setPage(currentPage).getLore())
                .finish();
    }

    public static ItemStack previousPage(Player p, int currentPage) {
        return IItemBuilder
                .create(Material.DOUBLE_PLANT)
                .setName(GUIMessages.faction_toplist_previous.language(p).setPage(currentPage).getName())
                .setLore(GUIMessages.faction_toplist_previous.language(p).setPage(currentPage).getLore())
                .finish();
    }
}
