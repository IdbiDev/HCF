package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Reclaim.ReclaimConfig;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

@CommandInfo(
        name = "reclaim",
        description = "Gives a basic start kit. You can only use it once!",
        permission = "factions.commands.reclaim",
        syntax = "/reclaim")
public class ReclaimCommand extends HCFCommand {

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            if (ReclaimConfig.isClaimed(p)) {
                p.sendMessage(Messages.reclaim_already_claimed.language(p).queue());
                return;
            }
            reclaim(p);
            p.sendMessage(Messages.reclaim_claimed.language(p).queue());
        } else {
            p.sendMessage(Messages.no_permission.language(p).queue());
        }
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    public static void reclaim(Player p) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(ReclaimConfig.getRankName(p) == null) return;
        hcfPlayer.addLives(ReclaimConfig.getLives(p));
        List<String> commands = ReclaimConfig.getCommands(p);
        /*p.sendMessage("Kaptál életet: " + ReclaimConfig.getLives(p));
        p.sendMessage("Új életeid száma: " + hcfPlayer.getLives());*/

        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%" , p.getName()));
        }
        ReclaimConfig.claimed(p);
    }
}
