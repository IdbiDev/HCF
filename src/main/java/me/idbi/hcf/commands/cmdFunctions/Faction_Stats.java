package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.HistoryGUI.Player.PlayerStatInventory;
import me.idbi.hcf.CustomFiles.Comments.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Faction_Stats {


    public static void PlayerStats(Player p, String[] args) {
        if(args.length == 1) {
            p.openInventory(PlayerStatInventory.inv(p));
        } else if(args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if(target.isOnline()) {
                if(p.hasPermission("factions.command.stats")) {
                    p.openInventory(PlayerStatInventory.inv(target.getPlayer()));
                } else {
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            } else {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        }

        for (int i = 1; i < 50; i++) {
            p.performCommand("f deposit " + i);
        }
    }
}
