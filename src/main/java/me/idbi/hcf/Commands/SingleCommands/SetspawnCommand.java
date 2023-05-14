package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.HCFServer;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
@CommandInfo(
        name = "setspawn",
        description = "Set spawn of your world",
        permission = "factions.commands.admin.setspawn",
        syntax = "/setspawn")

public class SetspawnCommand extends HCFCommand {

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        World world = p.getWorld();
        if (world.getName().equalsIgnoreCase(Config.WorldName.asStr()) || world.getName().equalsIgnoreCase(Config.EndName.asStr()) || world.getName().equalsIgnoreCase(Config.NetherName.asStr())) {
            HCFServer.getServer().getMap(world).setWorldSpawn(p.getLocation());
            p.sendMessage(Messages.setspawn.language(p).setWorld(world).setCoords(
                    p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()
            ).queue());
        }
    }
}
