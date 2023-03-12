package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HistoryGUI.Player.PlayerStatInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

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
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction stats [player]";
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
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
