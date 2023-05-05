package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.WaypointPlayer;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "waypoint",
        description = "Toggle waypoints",
        permission = "factions.commands.waypoint",
        syntax = "/waypoint <show | hide>")
public class WaypointCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        WaypointPlayer waypointPlayer = hcfPlayer.getWaypointPlayer();
        if(args.length == 0) {
            if(waypointPlayer.toggleWaypoints()) {
                p.sendMessage(Messages.waypoint_enabled.language(p).queue());
                addCooldown(p);
            } else {
                p.sendMessage(Messages.waypoint_disabled.language(p).queue());
                addCooldown(p);
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("show")) {
                waypointPlayer.showDefault();
                p.sendMessage(Messages.waypoint_enabled.language(p).queue());
                addCooldown(p);
            }
            else if(args[0].equalsIgnoreCase("hide")) {
                waypointPlayer.hideDefaults();
                p.sendMessage(Messages.waypoint_disabled.language(p).queue());
                addCooldown(p);
            }
        } else {
            printUsage(p);
        }
    }
}
