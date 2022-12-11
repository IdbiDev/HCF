package me.idbi.hcf.FrakcioGUI.Items;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GUI_Items {

    public static ItemStack rankManager() {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Ranks");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage ranks!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack blackGlass() {
        ItemStack is = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack playerManager() {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eManage Players");
        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to manage members!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack back() {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§cBack");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack memberHead(String name) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(name);
        im.setDisplayName("§e" + name);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack factinoStats(Main.Faction faction) {
        ItemStack is = new ItemStack(Material.COMPASS);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§2§o" + faction.name);
        String homeLocation;
        if(faction.homeLocation != null)
            homeLocation = faction.homeLocation.getBlockX() + ", " + faction.homeLocation.getBlockY() + ", " + faction.homeLocation.getBlockZ();
        else homeLocation = "-";

        im.setLore(Arrays.asList(
                "§7┌──",
                "§7│ §aDTR §7/ §aMax DTR: §f" + faction.DTR + " §7/ §f" + faction.DTR_MAX,
                "§7│ §aDTR Regen: §f" + ((faction.DTR == faction.DTR_MAX) ? "-" : playertools.convertLongToTime(faction.DTR_TIMEOUT)),
                "§7│ §aBalance: §f$" + faction.balance,
                "§7│ §aMember Count: §f" + faction.memberCount,
                "§7│ §aHome Location: §f" + homeLocation,
                "§7├──",
                "§7│ §aKills: §f" + faction.getKills(),
                "§7│ §aDeaths: §f" + faction.getDeaths(),
                "§7└──"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack renameFaction() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eRename Faction");

        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to rename your faction!"
        ));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack histories() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§eFaction Histories");

        im.setLore(Arrays.asList(
                "§5",
                "§7Click here to see the histories!"
        ));
        is.setItemMeta(im);
        return is;
    }
}
