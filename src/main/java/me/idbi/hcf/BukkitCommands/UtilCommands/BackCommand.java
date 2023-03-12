package me.idbi.hcf.BukkitCommands.UtilCommands;

import com.google.common.collect.HashBiMap;
import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.List;

@CommandInfo(
        name = "back",
        description = "Teleport back to last position",
        permission = "factions.commands.back",
        adminPermission = "factions.commands.back.others",
        syntax = "/back [player]")
public class BackCommand extends HCFCommand implements Listener {

    public static HashMap<Player, Location> hashMap;

    @Override
    public void execute(Player p, String[] args) {
        if(args.length == 0) {
            if (hashMap.containsKey(p)) {
                p.teleport(hashMap.get(p));
                p.sendMessage(Messages.back_successfully.language(p).queue());
            } else {
                p.sendMessage(Messages.no_last_location.language(p).queue());
            }
        } else if(args.length == 1) {
            if(p.hasPermission(getCommandInfo().adminPermission())) {
                backOther(p, args[0]);
            } else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        } else {
            if(p.hasPermission(getCommandInfo().adminPermission())) {
                p.sendMessage("§cUsage: " + getCommandInfo().syntax());
            }
        }
    }

    @Override
    public void execute(ConsoleCommandSender ccs, String[] args) {
        if(args.length == 1) {
            backOther(ccs, args[0]);
        } else {
            ccs.sendMessage("§cUsage: " + getCommandInfo().syntax());
        }
    }

    private void backOther(CommandSender sender, String name) {
        if(BukkitCommandManager.findTarget(sender, name)) {
            Player target = BukkitCommandManager.getTarget(name);
            if(hashMap.containsKey(target)) {
                target.teleport(hashMap.get(target));
                sender.sendMessage(Messages.back_successfully.queue());
                return;
            }
            sender.sendMessage(Messages.console_no_last_location.queue());
            return;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        hashMap.put(e.getEntity(), e.getEntity().getLocation());
    }

    @EventHandler
    public void onDeath(PlayerTeleportEvent e) {
        hashMap.put(e.getPlayer(), e.getFrom());
    }
}
