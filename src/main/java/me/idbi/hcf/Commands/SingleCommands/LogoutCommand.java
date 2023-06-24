package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.BungeeChanneling;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@CommandInfo(
        name = "logout",
        description = "Select language",
        permission = "factions.commands.logout",
        syntax = "/logout")
public class LogoutCommand extends HCFCommand implements Listener  {
    @Override
    public void execute(Player p, String[] args) {
        // /revive <player>
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

    @Override
    public int getCooldown() {
        return 0;
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

