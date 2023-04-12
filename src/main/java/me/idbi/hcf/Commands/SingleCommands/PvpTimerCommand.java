package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.MiscTimers;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "pvp",
        description = "Pvp Enable",
        permission = "factions.commands.pvptimer",
        syntax = "/pvp enable")
public class PvpTimerCommand extends HCFCommand {
    MiscTimers miscTimers = new MiscTimers();

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("enable")) {
                if(Timers.PVP_TIMER.has(p)) {
                    Timers.PVP_TIMER.remove(p);
                    p.sendMessage(Messages.pvp_timer_enable.language(p).queue());
                    miscTimers.removeFakeWalls(p);

                } else {
                    p.sendMessage(Messages.pvp_timer_already_enable.language(p).queue());
                }
                addCooldown(p);
            }
        }
    }
}
