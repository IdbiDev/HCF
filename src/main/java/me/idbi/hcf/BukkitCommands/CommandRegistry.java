package me.idbi.hcf.BukkitCommands;

import me.idbi.hcf.BukkitCommands.UtilCommands.BackCommand;
import me.idbi.hcf.Main;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.List;

public class CommandRegistry {

    private static Main m = Main.getPlugin(Main.class);
    public static void register(List<HCFCommand> cmds) {
        for(HCFCommand cmd : cmds) {
            m.getCommand(cmd.getCommandInfo().name()).setExecutor(cmd);
        }
    }
}
