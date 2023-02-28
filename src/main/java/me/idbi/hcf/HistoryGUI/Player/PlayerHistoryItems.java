package me.idbi.hcf.HistoryGUI.Player;

import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PlayerHistoryItems {

    /*
        public int MoneySpend;
        public int MoneyEarned;
        public long TimePlayed;
        public long startDate;
        public long lastLogin;
        public int kills;
        public int deaths;
     */

    public static ItemStack stats(Player p) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6☰ §eStatistics");
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        PlayerStatistic stats = hcfPlayer.playerStatistic;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        im.setLore(Arrays.asList(
                "§5",
                "§6§l▎ §7Time Played: §e" + formatter.format(new Date(stats.TimePlayed - 60 * 60 * 1000L)),
                "§6§l▎ §7First Join: §e" + formatter2.format(new Date(stats.startDate - 3600 * 1000L)),
                "§6§l▎ §7Last Join: §e" + formatter2.format(new Date(stats.lastLogin - 3600 * 1000L)),
                "§5",
                "§6§l┏ §7Money Earned: §e$" + stats.MoneyEarned,
                "§6§l┗ §7Money Spend: §e$" + stats.MoneySpend,
                "§5",
                "§6§l▎ §7Kills: §e" + stats.kills,
                "§6§l▎ §7Deaths: §e" + stats.deaths

        ));
        is.setItemMeta(im);
        return is;
    }

    /*
        public long TotalBardClassTime;
        public long TotalAssassinClassTime;
        public long TotalArcherClassTime;
        public long TotalMinerClassTime;
        public long TotalClassTime;
     */
    public static ItemStack classStats(Player p) {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§2☰ §aClass Statistics");
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        PlayerStatistic stats = hcfPlayer.playerStatistic;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        long total = 0L;
        total += stats.TotalBardClassTime;
        total += stats.TotalAssassinClassTime;
        total += stats.TotalArcherClassTime;
        total += stats.TotalMinerClassTime;

        im.setLore(Arrays.asList(
                "§5",
                "§2§l▎ §7Bard: §a" + formatter.format(new Date(stats.TotalBardClassTime - 60 * 60 * 1000L)),
                "§2§l▎ §7Assassin: §a" + formatter.format(new Date(stats.TotalAssassinClassTime - 60 * 60 * 1000L)),
                "§2§l▎ §7Archer: §a" + formatter.format(new Date(stats.TotalArcherClassTime - 60 * 60 * 1000L)),
                "§2§l▎ §7Miner: §a" + formatter.format(new Date(stats.TotalMinerClassTime - 60 * 60 * 1000L)),
                "§5",
                "§2§l➸ §7Total: §a" + formatter.format(new Date(total - 3600000))

        ));
        is.setItemMeta(im);
        return is;
    }


}
