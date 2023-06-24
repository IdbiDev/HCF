package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFServer;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
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
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(args.length == 1 && (hcfPlayer.isInDuty() || HCFPermissions.admin_spawn.check(p))) {
            World.Environment env = World.Environment.NORMAL;
            if (args[0].equalsIgnoreCase("nether"))
                env = World.Environment.NETHER;
            if (args[0].equalsIgnoreCase("the_end") || args[0].equalsIgnoreCase("end") || args[0].equalsIgnoreCase("theend"))
                env = World.Environment.THE_END;

            p.teleport(HCFServer.getServer().getMap(env).getWorldSpawn());
            p.sendMessage(Messages.spawn_teleported.language(p).queue());
            return;
        }

        else if(args.length == 0) {
            if (Main.SOTWEnabled && Config.SOTWSpawnEnable.asBoolean()) {
                p.teleport(HCFServer.getServer().getMap(World.Environment.NORMAL).getWorldSpawn());
                p.sendMessage(Messages.spawn_teleported.language(p).queue());
            } else if (hcfPlayer.isInDuty()) {
                p.sendMessage(Messages.spawn_teleported.language(p).queue());
                p.teleport(HCFServer.getServer().getMap(World.Environment.NORMAL).getWorldSpawn());
            } else if(HCFPermissions.admin_spawn.check(p)) {
                p.sendMessage(Messages.spawn_teleported.language(p).queue());
                p.teleport(HCFServer.getServer().getMap(World.Environment.NORMAL).getWorldSpawn());
            } else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        }
    }
}
