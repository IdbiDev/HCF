package me.idbi.hcf.HistoryGUI.Player;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
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
        PlayerStatistic stats = hcfPlayer.getPlayerStatistic();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formatter.setTimeZone(Main.getTimeZone());
        formatter2.setTimeZone(Main.getTimeZone());
        /*System.out.println(new Date(stats.TimePlayed - 60 * 60 * 1000L));*/
        String str = Formatter.formatMMSS(stats.TimePlayed);
        im.setLore(Arrays.asList(
                "§5",
                "§6§l▎ §7Time Played: §e" + str,
                "§6§l▎ §7First Join: §e" + formatter2.format(new Date(stats.startDate )),
                "§6§l▎ §7Last Join: §e" + formatter2.format(new Date(stats.lastLogin )),
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
        PlayerStatistic stats = hcfPlayer.getPlayerStatistic();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(Main.getTimeZone());
        long total = 0L;
        total += stats.TotalBardClassTime;
        total += stats.TotalAssassinClassTime;
        total += stats.TotalArcherClassTime;
        total += stats.TotalMinerClassTime;

        im.setLore(Arrays.asList(
                "§5",
                "§2§l▎ §7Bard: §a" + Formatter.formatMMSS(stats.TotalBardClassTime),
                "§2§l▎ §7Assassin: §a" + Formatter.formatMMSS(stats.TotalAssassinClassTime),
                "§2§l▎ §7Archer: §a" + Formatter.formatMMSS(stats.TotalArcherClassTime),
                "§2§l▎ §7Miner: §a" + Formatter.formatMMSS(stats.TotalMinerClassTime),
                "§5",
                "§2§l➸ §7Total: §a" + Formatter.formatMMSS(total)

        ));
        is.setItemMeta(im);
        return is;
    }


}
