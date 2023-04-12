package me.idbi.hcf.FrakcioGUI.Items;

import me.idbi.hcf.CustomFiles.GUIMessages.GUIMessages;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUI_Items {

    public static ItemStack rankManager(Player p) {
        ItemStack is = new ItemStack(Material.NETHER_STAR);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(GUIMessages.faction_rank_manager.language(p).getName());
        im.setLore(GUIMessages.faction_rank_manager.language(p).getLore());
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

    public static ItemStack playerManager(Player p) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(GUIMessages.faction_member_manager.language(p).getName());
        im.setLore(GUIMessages.back_button.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack back(Player p) {
        ItemStack is = new ItemStack(Material.INK_SACK, 1, (byte) 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.back_button.language(p).getName());
        im.setLore(GUIMessages.back_button.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack memberHead(Player invViewer, String name) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();
        im.setOwner(name);
        im.setDisplayName(GUIMessages.member_head.language(invViewer).setPlayerName(name).getName());
        im.setLore(GUIMessages.member_head.language(invViewer).setPlayerName(name).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack factionStats(Player p, Faction faction) {
        ItemStack is = new ItemStack(Material.COMPASS);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_stats.language(p).setFaction(faction).getName());
        String homeLocation;
        if (faction.getHomeLocation() != null)
            homeLocation = faction.getHomeLocation().getBlockX() + ", " + faction.getHomeLocation().getBlockY() + ", " + faction.getHomeLocation().getBlockZ();
        else homeLocation = "-";

        String factionStatus = (Playertools.isFactionOnline(faction)
                ? Messages.status_design_online.language(p).queue()
                : Messages.status_design_offline.language(p).queue());
        String leaderName = "";
        try {
            leaderName = ((Bukkit.getPlayer(faction.getLeader())) != null
                    ? Bukkit.getPlayer(faction.getLeader()).getName()
                    : Bukkit.getOfflinePlayer(faction.getLeader()).getName());
        } catch (IllegalArgumentException ignore) {
        }

        im.setLore(GUIMessages.faction_stats.language(p).setupShow(
                faction.getName(), factionStatus, leaderName, String.valueOf(faction.getBalance()),
                String.valueOf(faction.getKills()),
                String.valueOf(faction.getDeaths()),
                homeLocation,
                String.valueOf(faction.getDTR()),
                ((faction.getDTR() == faction.getDTR_MAX()) ? "-" : Playertools.convertLongToTime(faction.getDTR_TIMEOUT())),
                String.valueOf(faction.getDTR_MAX()),
                String.valueOf(Playertools.getOnlineSize(faction)),
                String.valueOf(faction.getMembers().size()),
                (faction.getDTR() <= 0 ? "true" : "false"),
                String.valueOf(faction.getPoints())

        ).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack renameFaction(Player p) {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(GUIMessages.faction_rename.language(p).getName());
        im.setLore(GUIMessages.faction_rename.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack histories(Player p) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(GUIMessages.faction_histories.language(p).getName());
        im.setLore(GUIMessages.faction_histories.language(p).getLore());
        is.setItemMeta(im);
        return is;
    }/*
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
        ));*/
}
