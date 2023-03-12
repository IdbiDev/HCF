package me.idbi.hcf.Reclaim.Commands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Reclaim.ReclaimConfig;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class ReclaimCommand implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("reclaim")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(args.length == 0) {
                    if(p.hasPermission("factions.commands.reclaim")) {
                        if(ReclaimConfig.isClaimed(p)) {
                            p.sendMessage(Messages.reclaim_already_claimed.language(p).queue());
                            return false;
                        }
                        reclaim(p);
                        p.sendMessage(Messages.reclaim_claimed.language(p).queue());
                    } else {
                        p.sendMessage(Messages.no_permission.language(p).queue());
                    }
                }
            }
        }
        return false;
    }

    public static void reclaim(Player p) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        hcfPlayer.addLives(ReclaimConfig.getLives(p));
        List<String> commands = ReclaimConfig.getCommands(p);
        p.sendMessage("Kaptál életet: " + ReclaimConfig.getLives(p));
        p.sendMessage("Új életeid száma: " + hcfPlayer.lives);

        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%" , p.getName()));
        }
        ReclaimConfig.claimed(p);
    }
}
