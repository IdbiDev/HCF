package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.HCFServer;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "spawn",
        description = "Teleports to spawn",
        permission = "factions.commands.spawn",
        syntax = "/spawn")
public class SpawnCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        HCFServer.getServer().getMap(World.Environment.NORMAL).setWorldSpawn(p.getLocation());
        p.teleport(HCFServer.getServer().getMap(p.getWorld()).getWorldSpawn());
        p.sendMessage("Teleported overworld");
    }
}
