package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.MountainEvent;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "mountain",
        description = "Mountain",
        permission = "factions.commands.admin.mountain",
        syntax = "/mountain reload")
public class MountainCommand extends HCFCommand {
    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                MountainEvent.getInstance().reset();
                addCooldown(p);
            }
        } else {
            p.sendMessage(Messages.mountain_remaining.language(p).setTime(Formatter.getRemaining(MountainEvent.getInstance().getRemaining(), true)).queue());
        }
    }
}
