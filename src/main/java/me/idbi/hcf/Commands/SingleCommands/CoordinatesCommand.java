package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "coordinates",
        description = "Coordinates",
        permission = "factions.commands.coordinates",
        syntax = "/coordinates")
public class CoordinatesCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void execute(Player p, String[] args) {
        p.sendMessage(Messages.coordinates_command.language(p).queueList().toArray(new String[0]));
        addCooldown(p);
    }
}
