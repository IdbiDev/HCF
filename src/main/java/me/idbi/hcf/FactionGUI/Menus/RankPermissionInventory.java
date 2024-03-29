package me.idbi.hcf.FactionGUI.Menus;

import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.FactionGUI.Items.RP_Items;
import me.idbi.hcf.Tools.FactionRankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RankPermissionInventory {

    public static Inventory inv(Player p, FactionRankManager.Rank rank) {
        Inventory inv = Bukkit.createInventory(null, 5 * 9, rank.getName() + " Permissions");

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        inv.setItem(2, RP_Items.all());
        inv.setItem(4, RP_Items.kick());
        inv.setItem(6, RP_Items.basic());
        inv.setItem(29, RP_Items.invite());
        inv.setItem(31, RP_Items.manageRanks());
        inv.setItem(33, RP_Items.withdraw());

        ItemStack ALL = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_ALL) ? RP_Items.on() : RP_Items.off();
        ItemStack kick = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_KICK) ? RP_Items.on() : RP_Items.off();
        ItemStack basic = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_PLAYERS) ? RP_Items.on() : RP_Items.off();
        ItemStack invite = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_INVITE) ? RP_Items.on() : RP_Items.off();
        ItemStack sethome = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_RANKS) ? RP_Items.on() : RP_Items.off();
        ItemStack withdraw = rank.getClassPermissions().get(FactionRankManager.Permissions.MANAGE_MONEY) ? RP_Items.on() : RP_Items.off();

        inv.setItem(11, ALL);
        inv.setItem(13, kick);
        inv.setItem(15, basic);
        inv.setItem(38, invite);
        inv.setItem(40, sethome);
        inv.setItem(42, withdraw);

        inv.setItem(44, RP_Items.save(p));
        inv.setItem(36, RP_Items.cancel(p));

        return inv;
    }
}
