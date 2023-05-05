package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.BungeeChanneling;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class LogoutCommand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /revive <player>
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!HCFPermissions.logout.check(p)) return false;
            if (Timers.LOGOUT.has(p)) {
                // Todo: Already logging out
                //HCF_Timer.removeLogoutTimer(p);
                p.sendMessage(Messages.logout_timer_already_started.language(p).queue());

            } else {
                BungeeChanneling.getInstance().sendToLobby(p);
                Timers.LOGOUT.add(p);
                p.sendMessage(Messages.logout_timer_started.language(p).queue());
            }
        }
        return false;
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (Timers.LOGOUT.has(e.getPlayer())) {
                Timers.LOGOUT.remove(e.getPlayer());
                e.getPlayer().sendMessage(Messages.logout_timer_interrupted.language(e.getPlayer()).queue());
            }
        }
    }
}

