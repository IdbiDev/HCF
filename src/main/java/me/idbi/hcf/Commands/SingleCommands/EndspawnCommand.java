package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.HCFServer;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "endspawn",
        description = "Teleports to the end spawn",
        permission = "factions.commands.endspawn",
        syntax = "/endspawn")
public class EndspawnCommand extends HCFCommand {
    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        HCFServer.getServer().getMap(World.Environment.THE_END).setWorldSpawn(p.getLocation());
        p.teleport(HCFServer.getServer().getMap(p.getWorld()).getWorldSpawn());
        p.sendMessage("Teleported end");
    }
}
