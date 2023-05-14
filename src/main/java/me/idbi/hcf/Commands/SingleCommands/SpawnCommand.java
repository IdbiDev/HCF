package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFServer;
import net.minecraft.server.v1_8_R3.WorldGenEnder;
import org.bukkit.World;
import org.bukkit.entity.Player;

@CommandInfo(
        name = "spawn",
        description = "Teleports to spawn",
        permission = "factions.commands.spawn",
        syntax = "/spawn [world]")
public class SpawnCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        //HCFServer.getServer().getMap(World.Environment.NORMAL).setWorldSpawn(p.getLocation());
        if(args.length == 1) {
            World.Environment env = World.Environment.NORMAL;
            if(args[0].equalsIgnoreCase("nether"))
                env = World.Environment.NETHER;
            if(args[0].equalsIgnoreCase("the_end") || args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("theend"))
                env = World.Environment.THE_END;

            p.teleport(HCFServer.getServer().getMap(env).getWorldSpawn());
        } else {
            p.teleport(HCFServer.getServer().getMap(World.Environment.NORMAL).getWorldSpawn());
        }
        p.sendMessage(Messages.spawn_teleported.language(p).queue());
    }
}
