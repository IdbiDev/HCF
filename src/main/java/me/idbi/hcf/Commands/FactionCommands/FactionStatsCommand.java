package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HistoryGUI.Player.PlayerStatInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionStatsCommand extends SubCommand {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Shows the player statistics.";
    }

    @Override
    public String getSyntax() {
        return "/faction stats [player]";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (args.length == 1) {
            p.openInventory(PlayerStatInventory.inv(p));
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.isOnline()) {
                if (p.hasPermission("factions.commands.stats")) {
                    addCooldown(p);
                    p.openInventory(PlayerStatInventory.inv(target.getPlayer()));
                } else {
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            } else {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        }
    }
}
