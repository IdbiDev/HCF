package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.HCFServer;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "netherspawn",
        description = "Teleports to the nether spawn",
        permission = "factions.commands.netherspawn",
        syntax = "/netherspawn")
public class NetherspawnCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        HCFServer.getServer().getMap(World.Environment.NETHER).setWorldSpawn(p.getLocation());
        p.teleport(HCFServer.getServer().getMap(p.getWorld()).getWorldSpawn());
        p.sendMessage("Teleported nether");
    }
}
