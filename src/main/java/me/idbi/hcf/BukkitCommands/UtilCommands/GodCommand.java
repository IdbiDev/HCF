package me.idbi.hcf.BukkitCommands.UtilCommands;

import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.ArrayList;

@CommandInfo(
        name = "god",
        description = "Toggle god mode",
        permission = "factions.commands.god",
        adminPermission = "factions.commands.god.others",
        syntax = "/god [player]")
public class GodCommand extends HCFCommand implements Listener {

    public static ArrayList<Player> hashMap;

    @Override
    public void execute(Player p, String[] args) {

        if (args.length == 0) {
            if (hashMap.contains(p)) {
                hashMap.remove(p);
                p.sendMessage(Messages.god_disable.language(p).queue());
            } else {
                hashMap.add(p);
                p.sendMessage(Messages.god_enable.language(p).queue());
            }
        } else if (args.length == 1) {
            if (p.hasPermission(getCommandInfo().adminPermission())) {
                backOther(p, args[0]);
            } else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        } else {
            if (p.hasPermission(getCommandInfo().adminPermission())) {
                p.sendMessage("§cUsage: " + getCommandInfo().syntax());
            }
        }
    }

    @Override
    public void execute(ConsoleCommandSender ccs, String[] args) {
        if (args.length == 1) {
            backOther(ccs, args[0]);
        } else {
            ccs.sendMessage("§cUsage: " + getCommandInfo().syntax());
        }
    }

    private void backOther(CommandSender sender, String name) {
        if(BukkitCommandManager.findTarget(sender, name)) {
            Player target = BukkitCommandManager.getTarget(name);
            Messages msg = (sender instanceof Player) ? Messages.god_disable_other.language((Player) sender) : Messages.god_disable_other;
            if (hashMap.contains(target)) {
                hashMap.remove(target);
                target.sendMessage(Messages.god_disable.language(target).queue());
            } else {
                msg = (sender instanceof Player) ? Messages.god_enable_other.language((Player) sender) : Messages.god_enable_other;
                hashMap.add(target);
                target.sendMessage(Messages.god_enable.language(target).queue());
            }
            sender.sendMessage(msg.setPlayer(target).queue());
        }
    }

    @EventHandler
    public void whenImmortal(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hashMap.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void foodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hashMap.contains(player)) {
                event.setCancelled(true);
            }
        }
    }
}
