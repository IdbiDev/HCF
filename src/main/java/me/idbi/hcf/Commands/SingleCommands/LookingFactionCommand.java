package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "lookingforfaction",
        description = "Looking For Faction",
        permission = "factions.commands.lookingforfaction",
        syntax = "/lookingforfaction")
public class LookingFactionCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public void execute(Player p, String[] args) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(Messages.looking_for_faction.language(online).setPlayer(p).queue());
        }
        addCooldown(p);
    }
}
